package com.kieslect.user.controller;

import com.kieslect.api.RemoteFileService;
import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.user.domain.UserHealthSportLog;
import com.kieslect.user.proto.KActivityProto;
import com.kieslect.user.service.IUserHealthSportLogService;
import com.kieslect.user.utils.ByteArrayMultipartFile;
import com.kieslect.user.utils.ProtobufParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/healthSport")
public class HealthSportController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RemoteFileService remoteFileService;

    @Autowired
    private IUserHealthSportLogService userHealthSportLogService;

    @PostMapping("/upload")
    public R<?> upload(HttpServletRequest request, @RequestParam("file") @NotNull(message = "文件不能为空") MultipartFile file) throws IOException {
        try {
            // 获取登录用户信息
            LoginUserInfo loginUser = tokenService.getLoginUser(request);
            String userId = String.valueOf(loginUser.getId());

            // 获取文件输入流
            InputStream inputStream = file.getInputStream();

            // 解析上传的 Protocol Buffers 消息
            KActivityProto.KActivity kActivity = ProtobufParser.parseFromInputStream(inputStream);

            // 获取消息中的数据列表
            List<KActivityProto.KActivityListData> list = kActivity.getListList();

            // 获取文件夹路径
            String folderPath = getHealthSportDownloadFilePath(userId);

            // 获取文件夹下所有文件名列表
            ResponseEntity<List<String>> listFilesInFolder = remoteFileService.listFilesInFolder(folderPath);
            List<String> fileNames = listFilesInFolder.getBody();
            // 创建 MultipartFile 对象
            MultipartFile multipartFile = file;

            // 遍历文件列表，处理每个文件
            for (String fileName : fileNames) {
                String uploadFileName = getUploadFileName(fileName);
                // 下载文件内容并构建消息 Builder
                ResponseEntity<byte[]> response = remoteFileService.downloadFileByFilePath(fileName);
                if (response.getBody() != null) {
                    // 创建 KActivityProto.KActivity.Builder 实例
                    KActivityProto.KActivity.Builder kActivityBuilder = KActivityProto.KActivity.newBuilder();
                    // 获取输入流，并传递给解析方法
                    // 获取输入流
                    byte[] fileBytes = response.getBody();

                    kActivityBuilder.mergeFrom(fileBytes);

                    // 将新数据添加到 Builder 中
                    kActivityBuilder.addAllList(list);

                    // 序列化消息 Builder
                    byte[] serializedBytes = ProtobufParser.serializeBuilder(kActivityBuilder);

                    multipartFile = new ByteArrayMultipartFile(
                            "file", uploadFileName, "application/octet-stream", serializedBytes);

                }
            }
            // 调用远程文件上传服务
            R<?> uploadResult = remoteFileService.uploadFile(multipartFile, 6, Long.parseLong(userId));
            if (R.isError(uploadResult)) {
                throw new RuntimeException("文件上传失败: " + uploadResult.getMsg());
            }

            // 文件上传成功后记录日志
            HashMap<String, Object> uploadResultData = (HashMap<String, Object>) uploadResult.getData();
            UserHealthSportLog log = new UserHealthSportLog();
            log.setUserId(loginUser.getId().intValue());
            log.setUploadFile(uploadResultData.get("fileUrl").toString());
            log.setUploadFileSize((double) file.getSize());
            log.setCreateTime(Instant.now().getEpochSecond());
            log.setUpdateTime(Instant.now().getEpochSecond());
            userHealthSportLogService.save(log);

            return R.ok("文件上传成功");
        } catch (IOException e) {
            throw new RuntimeException("文件处理发生错误: " + e.getMessage());
        }
    }

    private String getUploadFileName(String fileName) {
        // 使用字符串分割方法获取最后一个斜杠后的部分
        String[] parts = fileName.split("/");
        String result = parts[parts.length - 1];
        return result;
    }


    private String getHealthSportDownloadFilePath(String userId) {
        return "healthsportdata/" + userId;
    }


    @GetMapping("/getAllData")
    public ResponseEntity<byte[]> getAllData(HttpServletRequest request) throws IOException {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        String userId = String.valueOf(loginUser.getId());

        // 查询用户最新的健康运动日志
        List<UserHealthSportLog> userHealthSportLogs = userHealthSportLogService.lambdaQuery()
                .eq(UserHealthSportLog::getUserId, userId)
                .orderByDesc(UserHealthSportLog::getUpdateTime).list();

        if (userHealthSportLogs.isEmpty()) {
            return ResponseEntity.ok().header("kcode", String.valueOf(404)).build(); // 没有找到日志，返回404
        }

        UserHealthSportLog latestLog = userHealthSportLogs.get(0);

        String uploadFile = latestLog.getUploadFile();
        String fileName = getUploadFileName(uploadFile);

        String filePath = getHealthSportDownloadFilePath(userId) + "/" + fileName;

        // 返回响应
        ResponseEntity<byte[]> downloadResponse = remoteFileService.downloadFileByFilePath(filePath);

        // 检查文件下载的响应状态码
        if (downloadResponse.getStatusCode().is2xxSuccessful() && downloadResponse.getBody() == null) {
            // 文件未找到，返回空响应
            return ResponseEntity.ok().header("kcode", String.valueOf(404)).build(); // 返回空响应
        }

        // 设置响应头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", fileName);
        headers.set("kcode", String.valueOf(200));

        return ResponseEntity.ok()
                .headers(headers)
                .body(downloadResponse.getBody());

    }
}
