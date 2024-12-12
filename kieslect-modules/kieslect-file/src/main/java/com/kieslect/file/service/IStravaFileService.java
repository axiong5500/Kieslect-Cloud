package com.kieslect.file.service;

import org.springframework.web.multipart.MultipartFile;

public interface IStravaFileService {
    int uploadFile(MultipartFile file, double offsetHours);
    void parseFileToDB();
    void getDBToTcx();
    void readTcxFileUploadStrava();
}
