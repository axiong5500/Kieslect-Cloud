package com.kieslect.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.AppHelpInfo;
import com.kieslect.device.service.IAppHelpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-06-29
 */
@RestController
@RequestMapping("/appHelpInfo")
public class AppHelpInfoController {

    @Autowired
    IAppHelpInfoService appHelpInfoService;
    @GetMapping("/sys/getList")
    public R<?> sysGetAppHelpInfoList() {
        return R.ok(appHelpInfoService.list());
    }

    @PostMapping("/sys/update")
    public R<?> updateAppHelpInfo(@RequestBody AppHelpInfo appHelpInfo) {
        appHelpInfo.setUpdateTime(Instant.now().getEpochSecond());
        return R.ok(appHelpInfoService.updateById(appHelpInfo));
    }


    @PostMapping("/sys/delete")
    public R<?> deleteAppHelpInfo(@RequestBody AppHelpInfo appHelpInfo) {
        LambdaQueryWrapper<AppHelpInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AppHelpInfo::getId, appHelpInfo.getId());
        return R.ok(appHelpInfoService.remove(lambdaQueryWrapper));
    }


    @PostMapping("/sys/save")
    public R<?> saveAppHelpInfo(@RequestBody AppHelpInfo appHelpInfo) {
        appHelpInfo.setCreateTime(Instant.now().getEpochSecond());
        appHelpInfo.setUpdateTime(Instant.now().getEpochSecond());
        return R.ok(appHelpInfoService.save(appHelpInfo));
    }
}
