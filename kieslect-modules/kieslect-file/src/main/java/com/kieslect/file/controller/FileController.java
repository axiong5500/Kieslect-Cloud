package com.kieslect.file.controller;

import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.core.enums.ResponseCodeEnum;
import com.kieslect.file.config.OSSConfig;
import com.kieslect.file.domain.UploadResponse;
import com.kieslect.file.enums.PathTypeEnum;
import com.kieslect.file.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;

@RestController
@RequestMapping("")
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    private OSSConfig ossConfig;

    @PostMapping("/upload")
    public R<?> uploadFile(@RequestParam("file") @NotNull(message = "文件不能为空") MultipartFile file, @RequestParam("pathType") @NotNull(message = "路径类型不能为空") Integer pathType) throws IOException {
        // 检查上传的文件是否为空
        if (file.isEmpty()) {
            // 文件为空，返回错误信息
            return R.fail(ResponseCodeEnum.FILE_NOT_EMPTY);
        }
        // 根据枚举获取路径类型
        PathTypeEnum pathTypeEnum = PathTypeEnum.getByCode(pathType);
        return uploadFile(file, pathTypeEnum.getPath());
    }

    public R<?> uploadFile(MultipartFile file, String path) throws IOException {
        // 创建 OSS 文件夹路径
        String bucketName = ossConfig.getBucketName();
        fileService.createFolder(bucketName, path);

        // 在路径后添加文件名
        String filename = FilenameUtils.getName(file.getOriginalFilename());
        String ossFilePath = path + "/" + filename;

        // 上传文件到 OSS
        fileService.uploadFile(file, bucketName, ossFilePath);

        // 生成文件的URL
        String fileUrl = generateFileUrl(ossFilePath);

        // 构造响应体
        UploadResponse response = new UploadResponse()
                .setFilename(filename)
                .setFileUrl(fileUrl)
                .setFileSize(file.getSize());

        return R.ok(response);
    }

    private String generateFileUrl(String filePath) {
        // 在这里根据实际情况生成文件的URL，可以根据域名、路径等信息拼接成完整的URL
        // 获取context path
        String contextPath = ossConfig.getContextPath();

        // 获取应用名称
        String appName = ossConfig.getAppName();

        return appName + contextPath + "/download/" + filePath;
    }

    @GetMapping("/download/**")
    public ResponseEntity<InputStreamResource> downloadFile(HttpServletRequest request) throws IOException {
        // 获取请求中的路径参数
        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        // 获取文件名
        String filename = fullPath.substring(fullPath.lastIndexOf("/") + 1);
        String ossFilePath = fullPath.substring(fullPath.indexOf("/download/") + 10);
        try {
            OSSObject object = fileService.downloadFile(ossFilePath, ossConfig.getBucketName());
            java.io.InputStream inputStream = object.getObjectContent();

            // 根据文件名后缀设置 MIME 类型
            MediaType mediaType = MediaType.parseMediaType(getContentType(filename));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType); // 设置内容类型为二进制流
            headers.setContentLength(object.getObjectMetadata().getContentLength());

            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
        } catch (OSSException e) {
            // 文件不存在，返回 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    private String getContentType(String filename) {
        // 你可以根据文件名后缀来设置 MIME 类型，这里仅作示例
        String extension = FilenameUtils.getExtension(filename);
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            // 其他文件类型根据需要添加
            default:
                return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}
