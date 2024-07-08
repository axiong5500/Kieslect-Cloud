package com.kieslect.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.AppHelpInfo;
import com.kieslect.device.service.IAppHelpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    public R<?> sysGetDeviceManageList() {
        return R.ok(appHelpInfoService.list());
    }

    @PostMapping("/sys/update")
    public R<?> updateIcon(@RequestBody AppHelpInfo appHelpInfo) {
        appHelpInfo.setUpdateTime(LocalDateTime.now());
        return R.ok(appHelpInfoService.updateById(appHelpInfo));
    }


    @PostMapping("/sys/delete")
    public R<?> deleteIcon(@RequestBody AppHelpInfo appHelpInfo) {
        LambdaQueryWrapper<AppHelpInfo> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AppHelpInfo::getId, appHelpInfo.getId());
        return R.ok(appHelpInfoService.remove(lambdaQueryWrapper));
    }


    @PostMapping("/sys/save")
    public R<?> saveIcon(@RequestBody AppHelpInfo appHelpInfo) {
        appHelpInfo.setCreateTime(LocalDateTime.now());
        appHelpInfo.setUpdateTime(LocalDateTime.now());
        return R.ok(appHelpInfoService.save(appHelpInfo));
    }
}
