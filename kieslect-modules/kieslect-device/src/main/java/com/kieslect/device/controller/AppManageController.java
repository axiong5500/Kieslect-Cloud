package com.kieslect.device.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.AppManage;
import com.kieslect.device.service.IAppManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-04-01
 */
@RestController
@RequestMapping("/appManage")
public class AppManageController {
    @Autowired
    IAppManageService appManageService;
    @GetMapping("/sys/getList")
    public R<?> sysGetAppManageList() {
        return R.ok(appManageService.list());
    }
    // update
    @PostMapping("/sys/update")
    public R<?> updateAppManage(@RequestBody AppManage appManage) {
        return R.ok(appManageService.updateById(appManage));
    }
    // delete
    @PostMapping("/sys/delete")
    public R<?> deleteAppManage(@RequestBody AppManage appManage) {
        appManageService.removeById(appManage.getId());
        return R.ok();
    }

    @PostMapping("/sys/save")
    public R<?> saveAppManage(@RequestBody AppManage appManage) {
        appManageService.save(appManage);
        int savedId = appManage.getId();
        return R.ok(savedId);
    }
}
