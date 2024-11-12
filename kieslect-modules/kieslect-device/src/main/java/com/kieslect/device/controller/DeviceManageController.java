package com.kieslect.device.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.DeviceManage;
import com.kieslect.device.domain.vo.DeviceManageVO;
import com.kieslect.device.service.IDeviceMacRegionLockService;
import com.kieslect.device.service.IDeviceManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceManageController.class);

    @Autowired
    private IDeviceManageService deviceManageService;

    @Autowired
    private IDeviceMacRegionLockService deviceMacRegionLockService;


    /**
     * 获取设备是否在可用区域
     *
     * @param mac
     * @return 1，可用地区，0，不可用地区，默认值：1代表全球
     */
    @GetMapping("/getMacIsRegionLock")
    public R<?> getMacIsRegionLock(@RequestParam(value ="kId") Integer kId,
                                    @RequestParam(value ="mac") String mac,
                                    @RequestParam(value ="geoNameId") Integer geoNameId) {
        int result = deviceMacRegionLockService.getLock(kId,mac,geoNameId);
        return R.ok(result);
    }

    @GetMapping("/getList")
    public R<?> getDeviceManageList(@RequestParam(value = "deviceId", required = false) String deviceId,
                                    @RequestParam(value ="producers", required = false) Integer producers,
                                    @RequestParam(value ="appName", required = false) Integer appName) {
        List<DeviceManageVO> list = deviceManageService.getDeviceManageList(deviceId,producers,appName);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        return R.ok(result);
    }
    @GetMapping("/sys/getList")
    public R<?> sysGetDeviceManageList() {
        return R.ok(deviceManageService.list());
    }
    @PostMapping("/sys/update")
    public R<?> updateDeviceManage(@RequestBody DeviceManage deviceManage) {
        deviceManage.setUpdateTime(Instant.now().getEpochSecond());
        deviceManageService.updateEntity(deviceManage);
        return R.ok();
    }
    @PostMapping("/sys/updateTemplateFlag")
    public R<?> updateTemplateFlag(@RequestBody DeviceManage deviceManage) {
        UpdateWrapper<DeviceManage> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("form", deviceManage.getForm());
        updateWrapper.set("template_flag", deviceManage.getTemplateFlag());
        return R.ok(deviceManageService.update(updateWrapper));
    }
    @PostMapping("/sys/delete")
    public R<?> deleteDeviceManage(@RequestBody DeviceManage deviceManage) {
        return R.ok(deviceManageService.removeById(deviceManage.getId()));
    }
    @PostMapping("/sys/save")
    public R<?> saveDeviceManage(@RequestBody DeviceManage deviceManage) {
        deviceManage.setCreateTime(Instant.now().getEpochSecond());
        deviceManage.setUpdateTime(Instant.now().getEpochSecond());
        return R.ok(deviceManageService.save(deviceManage));
    }
}
