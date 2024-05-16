package com.kieslect.file.controller;

import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.core.enums.ResponseCodeEnum;
import com.kieslect.file.config.OSSConfig;
import com.kieslect.file.domain.UploadResponse;
import com.kieslect.file.enums.PathTypeEnum;
import com.kieslect.file.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("")
public class FileController {

    // 日志
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private OSSConfig ossConfig;

    @PostMapping("/upload")
    public R<?> uploadFile(@RequestParam("file") @NotNull(message = "文件不能为空") MultipartFile file,
                           @RequestParam("pathType") @NotNull(message = "路径类型不能为空") Integer pathType,
                           @RequestParam(value = "userId", required = false) Long userId) throws IOException {
        // 检查上传的文件是否为空
        if (file.isEmpty()) {
            // 文件为空，返回错误信息
            return R.fail(ResponseCodeEnum.FILE_NOT_EMPTY);
        }
        // 根据枚举获取路径类型
        PathTypeEnum pathTypeEnum = PathTypeEnum.getByCode(pathType);
        userId = userId == null ? 9999L : userId;
        return uploadFile(file, pathTypeEnum.getPath(), userId);
    }

    public R<?> uploadFile(MultipartFile file, String path, long userId) throws IOException {
        // 创建 OSS 文件夹路径
        String bucketName = ossConfig.getBucketName();
        fileService.createFolder(bucketName, path);

        // 在路径后添加文件名
        // 获取文件名和后缀名
        String originalFilename = file.getOriginalFilename();
        String baseName = getBaseName(originalFilename);
        String extension = getFileExtension(originalFilename);
        // 生成唯一性标识
        String uniqueFileName = baseName + "." + extension;
        String ossFilePath = path + "/" + userId + "/" + uniqueFileName;

        // 上传文件到 OSS
        fileService.uploadFile(file, bucketName, ossFilePath);


        // 生成文件的URL
        String fileUrl = generateFileUrl(ossFilePath);

        // 构造响应体
        UploadResponse response = new UploadResponse()
                .setFilename(uniqueFileName)
                .setFileUrl(fileUrl)
                .setFileSize(file.getSize());

        return R.ok(response);
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf('.') + 1);
    }

    private String getBaseName(String filename) {
        // 获取文件名的基本部分作为 baseName
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return filename; // 没有找到扩展名，返回整个文件名
        } else {
            return filename.substring(0, lastDotIndex);
        }
    }

    private String generateFileUrl(String filePath) {
        // 在这里根据实际情况生成文件的URL，可以根据域名、路径等信息拼接成完整的URL
        // 获取context path
        String contextPath = ossConfig.getContextPath();

        // 获取应用名称
        String appName = ossConfig.getAppName();

        return "/" + appName + contextPath + "/download/" + filePath;
    }

    @GetMapping("/listFilesInFolder")
    public ResponseEntity<byte[]> listFilesInFolder(@RequestParam("folderPath") String folderPath) {
        try {
            // 列举指定前缀（文件夹路径）下的所有文件
            ListObjectsRequest listRequest = new ListObjectsRequest(ossConfig.getBucketName());
            listRequest.setPrefix(folderPath); // 设置前缀为文件夹路径
            ObjectListing objectListing = fileService.listObjects(listRequest);

            // 创建 ByteArrayOutputStream 用于保存所有文件内容
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // 遍历文件列表，并获取文件名
            for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
                // 获取文件内容
                OSSObject ossObject = fileService.getObject(objectSummary.getKey(), ossConfig.getBucketName());
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = ossObject.getObjectContent().read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead); // 将文件内容写入 ByteArrayOutputStream
                }
                ossObject.getObjectContent().close(); // 关闭对象内容流
                // 记录文件名和大小
                logger.info("Retrieved file: {} - Size: {} bytes", objectSummary.getKey(), objectSummary.getSize());
            }

            // 将 ByteArrayOutputStream 中的内容作为字节数组返回
            byte[] fileBytes = outputStream.toByteArray();

            return ResponseEntity.ok()
                    .body(fileBytes);


        } catch (OSSException | IOException e) {
            // 处理 OSS 异常
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/downloadFileByFilePath")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filePath") String filePath) {
        try {
            if (!fileService.doesObjectExist(filePath, ossConfig.getBucketName())) {
                logger.error(filePath + "文件不存在");
                return ResponseEntity.ok().build();
            }
            // 下载指定文件
            OSSObject object = fileService.getObject(filePath, ossConfig.getBucketName());
            InputStream inputStream = object.getObjectContent();

            // 读取文件内容到字节数组
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byte[] fileBytes = byteArrayOutputStream.toByteArray();

            // 获取文件名
            String filename = getFileNameFromPath(filePath);

            // 设置响应头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // 设置内容类型为二进制流
            headers.setContentLength(fileBytes.length);
            headers.setContentDispositionFormData("attachment", filename); // 设置文件名

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);

        } catch (OSSException e) {
            // 处理 OSS 异常
            e.printStackTrace();
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFileNameFromPath(String filePath) {
        // 从文件路径中获取文件名
        return filePath.substring(filePath.lastIndexOf('/') + 1);
    }

    @GetMapping("/download/**")
    public ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request) throws IOException {
        // 获取请求中的路径参数
        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        // 获取文件名
        String filename = Paths.get(fullPath).getFileName().toString();
        String ossFilePath = fullPath.substring(fullPath.indexOf("/download/") + 10);
        try {
            OSSObject object = fileService.getObject(ossFilePath, ossConfig.getBucketName());
            InputStream inputStream = object.getObjectContent();

            // 根据文件名后缀设置 MIME 类型
            MediaType mediaType = MediaType.parseMediaType(getContentType(filename));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType); // 设置内容类型为二进制流
            headers.setContentLength(object.getObjectMetadata().getContentLength());
            // 设置文件名
            headers.setContentDispositionFormData("attachment", filename);

            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
        } catch (OSSException e) {
            // 文件不存在，返回 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    private String getContentType(String filename) throws IOException {
        Path path = Paths.get(filename);
        String mimeType = Files.probeContentType(path);
        if (mimeType == null) {
            // 如果无法检测到MIME类型，则返回默认的二进制流类型
            return "application/octet-stream";
        }
        return mimeType;
    }
}
