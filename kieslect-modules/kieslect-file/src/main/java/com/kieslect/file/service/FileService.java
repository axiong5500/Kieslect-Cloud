package com.kieslect.file.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class FileService {

    @Autowired
    private OSS ossClient;

    public void uploadFile(MultipartFile file, String bucketName, String ossFilePath) throws IOException {



        File tempFile = convertMultiPartFileToFile(file);
        try {
            ossClient.putObject(new PutObjectRequest(bucketName, ossFilePath, tempFile));
        } finally {
            tempFile.delete();
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

    public OSSObject downloadFile(String filename, String bucketName) throws IOException {
        return ossClient.getObject(new GetObjectRequest(bucketName, filename));
    }

    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
}
