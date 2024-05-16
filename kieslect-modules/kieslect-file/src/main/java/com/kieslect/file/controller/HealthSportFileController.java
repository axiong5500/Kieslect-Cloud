package com.kieslect.file.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.file.config.OSSConfig;
import com.kieslect.file.enums.PathTypeEnum;
import com.kieslect.file.service.FileService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Paths;

@RestController
@RequestMapping("/healthSport")
public class HealthSportFileController {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(HealthSportFileController.class);

    private static final String LOCAL_UPLOAD_FOLDER;

    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            LOCAL_UPLOAD_FOLDER = "D:" + File.separator + "data" + File.separator + "health_sport_files";
        } else {
            LOCAL_UPLOAD_FOLDER = File.separator + "var" + File.separator + "data" + File.separator + "health_sport_files";
        }
    }


    @Autowired
    private FileService fileService;

    @Autowired
    private OSSConfig ossConfig;

    @PostMapping("/uploadLocalFileToOSS")
    public R<?> uploadLocalFileToOSS(@RequestParam("userId") Long userId, @RequestParam("pathType") Integer pathType) {
        // 获取本地文件
        File localFile = getLocalFile(userId, pathType);
        if (localFile == null || !localFile.exists()) {
            logger.error("Local file does not exist");
            return R.fail("Local file does not exist");
        }

        // 上传文件到 OSS
        // 创建 OSS 文件夹路径
        String path = PathTypeEnum.getByCode(pathType).getPath();
        String bucketName = ossConfig.getBucketName();
        fileService.createFolder(bucketName, path);

        // 构建 OSS 存储路径
        String ossFilePath = path + "/" + userId + "/" + localFile.getName();


        boolean success = fileService.uploadFileToOSS(localFile, ossConfig.getBucketName(), ossFilePath);
        if (!success) {
            logger.error("Failed to upload file to OSS");
            return R.fail("Failed to upload file to OSS");
        }

        //删除本地文件
        localFile.delete();

        logger.info("Successfully uploaded file to OSS: {}", ossFilePath);
        return R.ok();
    }


    private File getLocalFile(Long userId, int fileType) {
        String localUploadFolder = Paths.get(LOCAL_UPLOAD_FOLDER, userId.toString(), "type_" + fileType).toString();
        File userFolder = new File(localUploadFolder);
        if (userFolder.exists()) {
            File[] files = userFolder.listFiles();
            if (files != null && files.length > 0) {
                return files[0];
            }
        }
        return null;
    }

}
