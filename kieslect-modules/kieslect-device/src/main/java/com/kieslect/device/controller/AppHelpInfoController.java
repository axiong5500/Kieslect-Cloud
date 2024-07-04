package com.kieslect.device.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.device.service.IAppHelpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
