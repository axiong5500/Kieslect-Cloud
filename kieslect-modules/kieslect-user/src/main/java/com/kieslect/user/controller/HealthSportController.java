package com.kieslect.user.controller;

import cn.hutool.core.util.IdUtil;
import com.kieslect.api.RemoteFileService;
import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.user.enums.FileTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;

@RestController
@RequestMapping("/healthSport")
public class HealthSportController {

    private static final Logger logger = LoggerFactory.getLogger(HealthSportController.class);
    private static final String LOCAL_UPLOAD_FOLDER;

    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            LOCAL_UPLOAD_FOLDER = "D:" + File.separator + "data" + File.separator + "health_sport_files";
        } else {
            LOCAL_UPLOAD_FOLDER = File.separator + "var" + File.separator + "data" + File.separator + "health_sport_files";
        }
    }

    private static final long MAX_LOCAL_STORAGE_SIZE = 50 * 1024 * 1024; // 50MB

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RemoteFileService remoteFileService;


    @PostMapping("/upload")
    public R<?> upload(HttpServletRequest request,
                       @RequestParam(value = "fileType", required = false) Integer fileType,
                       @RequestParam("file") @NotNull(message = "文件不能为空") MultipartFile file) {
        if (fileType == null) {
            fileType = FileTypeEnum.HEALTH_DATA.getCode(); // 设置默认值为1
        }
        if (fileType == FileTypeEnum.HEALTH_DATA.getCode()) {
            return uploadData(request, file, FileTypeEnum.HEALTH_DATA.getPathTypeCode());
        } else if (fileType == FileTypeEnum.SPORT_DATA.getCode()) {
            return uploadData(request, file, FileTypeEnum.SPORT_DATA.getPathTypeCode());
        }
        return R.fail("上传失败");
    }

    @GetMapping("/removeData")
    public R<?> removeData(HttpServletRequest request,
                           @RequestParam(value = "fileType", required = false) Integer fileType) {
        if (fileType == null) {
            fileType = FileTypeEnum.HEALTH_DATA.getCode(); // 设置默认值为1
        }
        if (fileType == FileTypeEnum.HEALTH_DATA.getCode()) {
            return removeLocalAndRemoteData(request, FileTypeEnum.HEALTH_DATA.getPathTypeCode());
        } else if (fileType == FileTypeEnum.SPORT_DATA.getCode()) {
            return removeLocalAndRemoteData(request, FileTypeEnum.SPORT_DATA.getPathTypeCode());
        }
        return R.fail("删除失败");
    }

    private R<?> removeLocalAndRemoteData(HttpServletRequest request, int pathTypeCode) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return R.fail("用户未登录");
        }
        if (loginUser.getUserKey() == null) {
            return R.fail("用户未登录");
        }
        if (loginUser.getKid() == null) {
            return R.fail("用户未登录");
        }
        //删除本地文件
        removeLocalData(loginUser.getKid(), pathTypeCode);
        //删除远程文件
        removeRemoteData(loginUser.getKid(), pathTypeCode);
        return R.ok();
    }

    private void removeRemoteData(Long userId, int fileType) {
        remoteFileService.removeOSSFile(userId, fileType);
    }

    private void removeLocalData(Long userId, int fileType) {
        if (userId == null) {
            return;
        }
        File healthDataFolder = new File(getLocalUploadFolder(userId, fileType));
        if (healthDataFolder.exists()) {
            File[] files = healthDataFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    logger.info("删除本地文件: {}", healthDataFolder.getAbsolutePath());
                    file.delete();
                }
            }
        }
    }

    public R<?> uploadData(HttpServletRequest request, MultipartFile file, int pathTypeCode) {

        try {
            // 获取登录用户信息
            LoginUserInfo loginUser = tokenService.getLoginUser(request);
            Long userId = loginUser.getKid();

            logger.info("开始上传文件，用户ID: {}, 文件类型: {}", userId, FileTypeEnum.getCodeByPathTypeCode(pathTypeCode).getDescription());

            File oldFile = getOldFile(userId, pathTypeCode);
            if (oldFile != null) {
                logger.info("发现旧文件，准备合并");
                mergeFiles(oldFile, file);
            } else {
                logger.info("无旧文件，准备保存新文件");
                saveNewFile(file, userId, pathTypeCode);
            }

            long totalLocalSize = getTotalLocalSize(userId, pathTypeCode);
            if (totalLocalSize > MAX_LOCAL_STORAGE_SIZE) {
                logger.info("本地存储文件大小超过限制，开始上传到OSS");
                remoteFileService.uploadLocalFileToOSS(userId, pathTypeCode);
            }

            logger.info("文件上传成功，返回成功消息");
            return R.ok("文件上传成功");
        } catch (Exception e) {
            logger.error("文件上传失败，错误信息: {}", e.getMessage());
            return R.fail(e.getMessage());
        }
    }


    // 获取指定用户ID的本地存储文件总大小
    private long getTotalLocalSize(Long userId, int fileType) {
        File userFolder = new File(getLocalUploadFolder(userId, fileType));
        long totalSize = 0;
        if (userFolder.exists() && userFolder.isDirectory()) {
            File[] files = userFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    totalSize += file.length();
                }
            }
        }
        return totalSize;
    }


    /**
     * 合并文件
     *
     * @param oldFile
     * @param newFile
     * @throws IOException
     */
    private void mergeFiles(File oldFile, MultipartFile newFile) throws IOException {

        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(oldFile, true));
             InputStream newFileStream = newFile.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = newFileStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    /**
     * 获取旧文件
     *
     * @param userId
     * @param fileType
     * @return
     */
    private File getOldFile(Long userId, int fileType) {
        File userFolder = new File(getLocalUploadFolder(userId, fileType));
        if (userFolder.exists()) {
            // 根据实际情况获取旧文件，例如根据文件名、日期等方式
            // 这里简单地假设旧文件名与userId相同
            // 获取当前目录下的文件
            File[] files = userFolder.listFiles();
            if (files != null && files.length > 0) {
                return files[0];
            }
        }
        return null;
    }

    // 保存新文件到本地服务器
    private void saveNewFile(MultipartFile newFile, Long userId, int fileType) throws IOException {
        // 创建用户ID对应的文件夹
        File userFolder = new File(getLocalUploadFolder(userId, fileType));
        if (!userFolder.exists()) {
            userFolder.mkdirs(); // 创建文件夹及其父文件夹
        }

        // 生成唯一的文件名，添加时间戳或随机字符串
        String originalFilename = newFile.getOriginalFilename();
        String uniqueFilename = generateUniqueFilename(originalFilename);


        // 保存新文件到用户文件夹
        File localFile = new File(userFolder, uniqueFilename);
        newFile.transferTo(localFile);
    }

    // 生成唯一的文件名，添加时间戳或随机字符串
    private String generateUniqueFilename(String originalFilename) {
        String randomString = IdUtil.simpleUUID(); // 生成随机的UUID
        String extension = getFileExtension(originalFilename); // 获取文件扩展名
        return randomString + extension;
    }

    // 获取文件扩展名
    private String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex != -1) {
            return filename.substring(dotIndex);
        }
        return ""; // 如果文件名中没有扩展名，则返回空字符串
    }

    // 根据用户ID获取本地上传文件夹路径
    private String getLocalUploadFolder(Long userId, int fileType) {
        return LOCAL_UPLOAD_FOLDER + File.separator + userId + File.separator + "type_" + fileType;
    }


    @GetMapping("/getHealthData")
    public ResponseEntity<byte[]> getHealthData(HttpServletRequest request) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getKid();
        int fileType = FileTypeEnum.HEALTH_DATA.getPathTypeCode();
        return getFile(userId, fileType);

    }


    @GetMapping("/getSportData")
    public ResponseEntity<byte[]> getSportData(HttpServletRequest request) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getKid();
        int fileType = FileTypeEnum.SPORT_DATA.getPathTypeCode();
        return getFile(userId, fileType);
    }

    public ResponseEntity<byte[]> getFile(Long userId, int fileType) {

        byte[] mergedFile;

        try {
            byte[] localFile = getLocalFile(userId, fileType);

            logger.info("用户ID: {}，本地文件获取成功", userId);
            if (localFile != null) {
                logger.info("用户ID: {}，本地文件大小: {}", userId, localFile.length);
            } else {
                logger.warn("用户ID: {}，本地文件为空", userId);
            }

            byte[] remoteFile = getRemoteFile(userId, fileType);

            logger.info("用户ID: {}，远程文件获取成功", userId);
            if (remoteFile != null) {
                logger.info("用户ID: {}，远程文件大小: {}", userId, remoteFile.length);
                mergedFile = mergeFiles(localFile, remoteFile);
                logger.info("用户ID: {}，文件合并成功，合并后文件大小: {}", userId, mergedFile.length);
            } else {
                logger.warn("用户ID: {}，远程文件为空", userId);
            }

            mergedFile = mergeFiles(localFile, remoteFile);

            if (mergedFile != null) {
                logger.info("用户ID: {}，文件合并成功，合并后文件大小: {}", userId, mergedFile.length);
            } else {
                logger.warn("用户ID: {}，文件合并为空", userId);
            }
        } catch (IOException e) {

            logger.error("用户ID: {}，获取文件时发生异常: {}", userId, e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "sportdata.kieslect");
        headers.set("kcode", String.valueOf(mergedFile != null ? 200 : 404));


        return ResponseEntity.ok()
                .headers(headers)
                .body(mergedFile);
    }

    private byte[] getLocalFile(Long userId, int fileType) throws IOException {
        File file = new File(getLocalUploadFolder(userId, fileType));
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                return Files.readAllBytes(files[0].toPath());
            }
        }
        return null;
    }

    private byte[] getRemoteFile(Long userId, int fileType) {
        String filePath = getSportDownloadFilePath(userId);
        if (fileType == FileTypeEnum.HEALTH_DATA.getPathTypeCode()) {
            filePath = getHealthSportDownloadFilePath(userId);
        }
        ResponseEntity<byte[]> downloadResponse = remoteFileService.listFilesInFolder(filePath);

        if (downloadResponse.getStatusCode().is2xxSuccessful() && downloadResponse.getBody() != null) {
            return downloadResponse.getBody();
        }
        return null;
    }

    private String getHealthSportDownloadFilePath(Long userId) {
        return "healthsportdata/" + userId;
    }

    private String getSportDownloadFilePath(Long userId) {
        return "sportdata/" + userId;
    }

    private byte[] mergeFiles(byte[] localFile, byte[] remoteFile) {
        if (localFile != null && remoteFile != null) {
            byte[] mergedFile = new byte[localFile.length + remoteFile.length];
            System.arraycopy(localFile, 0, mergedFile, 0, localFile.length);
            System.arraycopy(remoteFile, 0, mergedFile, localFile.length, remoteFile.length);
            return mergedFile;
        } else if (localFile != null) {
            return localFile;
        } else if (remoteFile != null) {
            return remoteFile;
        }
        return null;
    }
}
