package com.kieslect.file.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private OSS ossClient;

    public boolean uploadFileToOSS(File file, String bucketName, String ossFilePath) {
        try {
            // 检查文件是否存在
            if (file == null || !file.exists()) {
                logger.error("File does not exist: {}", file);
                return false;
            }

            // 上传文件到 OSS
            ossClient.putObject(new PutObjectRequest(bucketName, ossFilePath, file));
            logger.info("Uploaded file to OSS: {}", ossFilePath);
            return true;
        } catch (OSSException e) {
            logger.error("Failed to upload file to OSS: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("An unexpected error occurred: {}", e.getMessage());
            return false;
        }
    }

    public void uploadFile(MultipartFile file, String bucketName, String ossFilePath) throws IOException {

        File tempFile = convertMultiPartFileToFile(file);
        try {
            ossClient.putObject(new PutObjectRequest(bucketName, ossFilePath, tempFile));
        } finally {
            // 删除临时文件
            if (tempFile != null) {
                tempFile.delete();
            }
        }
    }

    public void createFolder(String bucketName, String folderPath) {
        // 在 OSS 中创建文件夹
        String[] segments = folderPath.split("/");
        StringBuilder ossFolderPath = new StringBuilder();
        for (String segment : segments) {
            ossFolderPath.append(segment).append("/");
            // 创建文件夹
            if (!ossClient.doesObjectExist(bucketName, ossFolderPath.toString())) {
                // 文件夹不存在则创建
                ossClient.putObject(bucketName, ossFolderPath.toString(), new ByteArrayInputStream(new byte[0]));
            }
        }
    }

    public OSSObject getObject(String filename, String bucketName)  {
        return ossClient.getObject(new GetObjectRequest(bucketName, filename));
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }

    public ObjectListing listObjects(ListObjectsRequest listRequest) {
        return ossClient.listObjects(listRequest);
    }

    public boolean doesObjectExist(String filename, String bucketName) {
        return ossClient.doesObjectExist(bucketName, filename);
    }

    public void removeOSSFile(String ossFilePath, String bucketName) {
        ossClient.deleteObject(bucketName, ossFilePath);
    }

    public void remoteUrlToOSS(String bucketName, String ossFilePath, InputStream inputStream) {
        ossClient.putObject(new PutObjectRequest(bucketName, ossFilePath, inputStream));
    }

    public void uploadFile(InputStream fileInputStream, String bucketName, String filePath,String contentType) {
        try  {
            // 创建 ObjectMetadata 对象并设置 MIME 类型
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            // 上传文件
            ossClient.putObject(new PutObjectRequest(bucketName, filePath, fileInputStream,metadata));
        } catch (Exception e) {
            throw new RuntimeException("文件上传到 OSS 失败", e);
        }
    }


}
