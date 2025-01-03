package com.kieslect.file.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.core.enums.ResponseCodeEnum;
import com.kieslect.file.config.OSSProperties;
import com.kieslect.file.domain.UploadResponse;
import com.kieslect.file.enums.PathTypeEnum;
import com.kieslect.file.service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("")
public class FileController {

    // 日志
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileService fileService;

    @Autowired
    private OSSProperties ossConfig;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${spring.application.name}")
    private String appName;

    @Value("${debugMode:false}")
    private String debugMode;

    public boolean isDebugMode() {
        return "true".equalsIgnoreCase(debugMode);
    }

    @PostMapping("/upload")
    public R<?> uploadFile(@RequestParam("file") @NotNull(message = "文件不能为空") MultipartFile file,
                           @RequestParam("pathType") @NotNull(message = "路径类型不能为空") Integer pathType,
                           @RequestParam(value = "userId", required = false) Long userId) throws IOException {
        logger.info("Received file upload request. Filename: {}, PathType: {}, UserId: {}",
                file.getOriginalFilename(), pathType, userId);
        if (file.isEmpty()) {
            logger.error("File is empty. Upload failed.");
            return R.fail(ResponseCodeEnum.FILE_NOT_EMPTY);
        }
        // 根据枚举获取路径类型
        PathTypeEnum pathTypeEnum = PathTypeEnum.getByCode(pathType);
        userId = userId == null ? 9999L : userId;

        try {
            // 调用实际的上传逻辑
            R<?> result = uploadFile(file, pathTypeEnum.getPath(), userId);
            logger.info("File uploaded successfully. Filename: {}", file.getOriginalFilename());
            return result;
        } catch (Exception e) {
            logger.error("File upload failed. Filename: {}", file.getOriginalFilename(), e);
            return R.fail("文件上传失败：" + e.getMessage());
        }
    }

    @PostMapping("/remoteUrlToOSS")
    public R<?> remoteUrlToOSS(@RequestParam("remoteUrl") String remoteUrl,
                               @RequestParam("pathType") Integer pathType) {

        // 根据枚举获取路径类型
        PathTypeEnum pathTypeEnum = PathTypeEnum.getByCode(pathType);
        String ossPath = pathTypeEnum.getPath();


        // 组装oss保存文件路径
        String uniqueFileName = UUID.randomUUID() + ".jpg"; // 唯一文件名
        String ossFilePath = ossPath + "/remote/" + uniqueFileName;

        // 获取bucket名称
        String bucketName = ossConfig.getBucketName();

        try {
            // 使用Hutool的HttpRequest下载远程文件到InputStream
            //开启代理IP和端口


            // 上传图片到OSS
            InputStream inputStream;
            if (isDebugMode()) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
                // 使用Hutool下载远程图片
                HttpResponse httpResponse = HttpUtil.createGet(remoteUrl).setProxy(proxy).setFollowRedirects(true).execute();
                byte[] imageBytes = httpResponse.bodyBytes();
                inputStream = new ByteArrayInputStream(imageBytes);
            }else{
                HttpResponse httpResponse = HttpUtil.createGet(remoteUrl).setFollowRedirects(true).execute();
                byte[] imageBytes = httpResponse.bodyBytes();
                inputStream = new ByteArrayInputStream(imageBytes);
            }

            // 将远程文件上传至OSS
            fileService.remoteUrlToOSS(bucketName, ossFilePath, inputStream);

            // 生成文件的URL
            String fileUrl = generateFileUrl(ossFilePath);

            // 关闭输入流
            inputStream.close();

            // 构造响应体
            UploadResponse response = new UploadResponse()
                    .setFilename(uniqueFileName)
                    .setFileUrl(fileUrl)
                    .setFileSize((long) -1); // 你可能需要一个方法来获取文件大小，这里暂时设为-1

            return R.ok(response);
        } catch (IOException e) {
            // 处理异常
            return R.fail("文件上传失败：" + e.getMessage());
        }
    }

    public R<?> uploadFile(MultipartFile file, String path, long userId) throws IOException {
        logger.info("Starting file upload. Filename: {}, Path: {}, UserId: {}",
                file.getOriginalFilename(), path, userId);
        // 创建 OSS 文件夹路径
        String bucketName = ossConfig.getBucketName();
        try {
            fileService.createFolder(bucketName, path);
            logger.info("OSS folder created successfully. Bucket: {}, Path: {}", bucketName, path);
        } catch (Exception e) {
            logger.error("Failed to create OSS folder. Bucket: {}, Path: {}", bucketName, path, e);
            return R.fail("创建 OSS 文件夹失败：" + e.getMessage());
        }

        // 在路径后添加文件名
        // 获取文件名和后缀名
        String originalFilename = file.getOriginalFilename();
        String baseName = getBaseName(originalFilename);
        String extension = getFileExtension(originalFilename);
        // 生成唯一性标识
        String uniqueFileName = baseName + "." + extension;
        String ossFilePath = path + "/" + userId + "/" + uniqueFileName;

        // 日志记录上传路径
        logger.info("Uploading file to OSS. Filename: {}, OSS FilePath: {}", uniqueFileName, ossFilePath);

        try {
            // 上传文件到 OSS
            fileService.uploadFile(file, bucketName, ossFilePath);
            logger.info("File uploaded successfully. Filename: {}, OSS FilePath: {}", uniqueFileName, ossFilePath);
        } catch (Exception e) {
            logger.error("Failed to upload file. Filename: {}, OSS FilePath: {}", uniqueFileName, ossFilePath, e);
            return R.fail("文件上传失败：" + e.getMessage());
        }


        // 生成文件的URL
        String fileUrl = generateFileUrl(ossFilePath);
        logger.info("File URL generated. Filename: {}, URL: {}", uniqueFileName, fileUrl);

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
