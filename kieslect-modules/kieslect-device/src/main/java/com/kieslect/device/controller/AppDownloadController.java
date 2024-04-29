package com.kieslect.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.AppDownload;
import com.kieslect.device.service.IAppDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-04-01
 */
@RestController
@RequestMapping("/appDownload")
public class AppDownloadController {

    @Autowired
    IAppDownloadService appDownloadService;

    @GetMapping("/sys/getList")
    public R<?> sysGetAppDownloadList() {
        return R.ok(appDownloadService.list());
    }

    //
    @PostMapping("/sys/update")
    public R<?> updateAppDownload(@RequestBody List<AppDownload> appDownloads) {
        return R.ok(appDownloadService.updateBatchById(appDownloads));
    }

    //
    @PostMapping("/sys/delete")
    public R<?> deleteAppDownload(@RequestBody AppDownload appDownload) {
        LambdaQueryWrapper<AppDownload> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AppDownload::getAppId, appDownload.getAppId());
        appDownloadService.remove(wrapper);
        return R.ok();
    }

    //
    @PostMapping("/sys/save")
    public R<?> saveAppDownload(@RequestBody AppDownload appDownload) {
        appDownloadService.save(appDownload);
        return R.ok();
    }
}
