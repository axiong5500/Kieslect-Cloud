package com.kieslect.file.task;

import com.kieslect.file.service.IStravaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class SportDataTaskController {
    @Autowired
    private IStravaFileService stravaFileService;
    @GetMapping("/parseFileToDB")
    public void parseFileToDB() {
        stravaFileService.parseFileToDB();
    }

    @GetMapping("/getDBToTcx")
    public void getDBToTcx() {
        stravaFileService.getDBToTcx();
    }

    @GetMapping("/readTcxFileUploadStrava")
    public void readTcxFileUploadStrava() {
        stravaFileService.readTcxFileUploadStrava();
    }
}
