package com.kieslect.device.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.DeviceManage;
import com.kieslect.device.domain.vo.DeviceManageVO;
import com.kieslect.device.service.IDeviceManageService;
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
@RequestMapping("")
public class DeviceManageController {
    @Autowired
    private IDeviceManageService deviceManageService;


    @GetMapping("/getList")
    public R<List<DeviceManageVO>> getDeviceManageList() {
        List<DeviceManageVO> result = deviceManageService.getDeviceManageList();
        return R.ok(result);
    }
    @GetMapping("/sys/getList")
    public R<?> sysGetDeviceManageList() {
        return R.ok(deviceManageService.list());
    }
    @PostMapping("/sys/update")
    public R<?> updateDeviceManage(@RequestBody DeviceManage deviceManage) {
        deviceManageService.updateEntity(deviceManage);
        return R.ok();
    }
    @PostMapping("/sys/delete")
    public R<?> deleteDeviceManage() {
        return R.ok(deviceManageService.list());
    }
    @PostMapping("/sys/save")
    public R<?> saveDeviceManage(@RequestBody DeviceManage deviceManage) {
        deviceManageService.save(deviceManage);
        return R.ok();
    }
}
