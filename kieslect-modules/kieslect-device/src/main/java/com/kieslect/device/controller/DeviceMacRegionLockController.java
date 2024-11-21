package com.kieslect.device.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.DeviceMacRegionLock;
import com.kieslect.device.domain.vo.DeviceMacRegionLockVO;
import com.kieslect.device.service.IDeviceMacRegionLockService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-11-15
 */
@RestController
@RequestMapping("/deviceMacRegionLock")
public class DeviceMacRegionLockController {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceMacRegionLockController.class);

    @Autowired
    IDeviceMacRegionLockService deviceMacRegionLockService;

    @PostMapping("/sys/getList")
    private R<Page<DeviceMacRegionLockVO>> sysGetRegionLockList(@RequestBody(required = false) DeviceMacRegionLock deviceMacRegionLock,
                                                                @RequestParam(name="pageNum", defaultValue = "1") int pageNum,
                                                                @RequestParam(name="pageSize",defaultValue = "10") int pageSize){
        return R.ok(deviceMacRegionLockService.getDeviceMacRegionLockWithPolicy(deviceMacRegionLock, pageNum, pageSize));
    }

    @PostMapping("/sys/update")
    public R<?> updateRegionLock(@RequestBody DeviceMacRegionLock deviceMacRegionLock) {
        deviceMacRegionLock.setUpdateTime(Instant.now().getEpochSecond());
        return R.ok(deviceMacRegionLockService.updateById(deviceMacRegionLock));
    }


    @PostMapping("/sys/delete")
    public R<?> deleteRegionLock(@RequestBody DeviceMacRegionLock deviceMacRegionLock) {
        return R.ok(deviceMacRegionLockService.removeById(deviceMacRegionLock.getId()));
    }


    @PostMapping("/sys/save")
    public R<?> saveRegionLock(@RequestBody DeviceMacRegionLock deviceMacRegionLock) {
        Long now = Instant.now().getEpochSecond();
        deviceMacRegionLock.setCreateTime(now);
        deviceMacRegionLock.setUpdateTime(now);
        return R.ok(deviceMacRegionLockService.save(deviceMacRegionLock));
    }

    @PostMapping("/sys/batchSave")
    public R<?> batchSaveRegionLock(@RequestBody List<DeviceMacRegionLock> deviceMacRegionLocks) {
        return R.ok(deviceMacRegionLockService.saveBatch(deviceMacRegionLocks));
    }

}
