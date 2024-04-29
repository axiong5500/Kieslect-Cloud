package com.kieslect.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.AppDescription;
import com.kieslect.device.service.IAppDescriptionService;
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
@RequestMapping("/appDesc")
public class AppDescriptionController {

    @Autowired
    IAppDescriptionService appDescriptionService;

    @GetMapping("/sys/getList")
    public R<?> sysGetDeviceManageList() {
        return R.ok(appDescriptionService.list());
    }


    @PostMapping("/sys/update")
    public R<?> updateAppDescription(@RequestBody List<AppDescription> appDescription) {
        return R.ok(appDescriptionService.updateBatchById(appDescription));
    }


    @PostMapping("/sys/delete")
    public R<?> deleteAppDescription(@RequestBody AppDescription appDescription) {
        LambdaQueryWrapper<AppDescription> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(AppDescription::getAppId, appDescription.getAppId());
        appDescriptionService.remove(lambdaQueryWrapper);
        return R.ok();
    }


    @PostMapping("/sys/save")
    public R<?> saveAppDescription(@RequestBody AppDescription appDescription) {
        appDescriptionService.save(appDescription);
        return R.ok();
    }

}
