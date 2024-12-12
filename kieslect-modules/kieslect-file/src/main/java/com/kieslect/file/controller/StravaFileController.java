package com.kieslect.file.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.file.service.IStravaFileService;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/strava")
public class StravaFileController {

    @Autowired
    private IStravaFileService stravaFileService;

    @PostMapping("/upload")
    public R<?> uploadFile(@RequestParam("file") @NotNull(message = "文件不能为空") MultipartFile file,@RequestParam("offsetHours")double offsetHours) {
        int result = stravaFileService.uploadFile(file,offsetHours);
        return R.ok(result);
    }
}
