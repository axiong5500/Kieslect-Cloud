package com.kieslect.device.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.ModuleConfig;
import com.kieslect.device.service.IModuleConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-04-05
 */
@RestController
@RequestMapping("/moduleConfig")
public class ModuleConfigController {
    @Autowired
    IModuleConfigService moduleConfigService;
    @GetMapping("/sys/getList")
    public R<?> getModuleConfigList() {
        return R.ok(moduleConfigService.list());
    }
    @PostMapping("/sys/save")
    public R<?> saveModuleConfig(@RequestBody ModuleConfig moduleConfig) {
        String now = String.valueOf(Instant.now().getEpochSecond());
        moduleConfig.setEnable((byte) 1).setCreateTime(now).setUpdateTime(now);
        moduleConfigService.save(moduleConfig);
        return R.ok();
    }

    @PostMapping("/sys/delete")
    public R<?> deleteModuleConfig(@RequestBody ModuleConfig moduleConfig) {
        moduleConfigService.removeById(moduleConfig.getId());
        return R.ok();
    }

    @PostMapping("/sys/update")
    public R<?> updateModuleConfig(@RequestBody ModuleConfig moduleConfig) {
        moduleConfig.setUpdateTime(String.valueOf(Instant.now().getEpochSecond()));
        moduleConfigService.updateById(moduleConfig);
        return R.ok();
    }

}
