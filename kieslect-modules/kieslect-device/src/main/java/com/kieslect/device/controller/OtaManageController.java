package com.kieslect.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.OtaManage;
import com.kieslect.device.service.IOtaManageService;
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
 * @since 2024-08-02
 */
@RestController
@RequestMapping("/ota")
public class OtaManageController {
    @Autowired
    private IOtaManageService otaManageService;
    @GetMapping("/getList")
    public R<?> getOtaManageList(@RequestParam(value = "innerId") Long deviceInnerId,
                                 @RequestParam(value ="otaVersion", required = false) String otaVersion ) {
        List<OtaManage> list = otaManageService.getOtaByDeviceInnerIdAndVersion(deviceInnerId,otaVersion);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        return R.ok(result);
    }

    @GetMapping("/sys/getList")
    public R<?> sysGetOtaManageList() {
        LambdaQueryWrapper<OtaManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(OtaManage::getCreateTime); // 按照创建时间降序
        return R.ok(otaManageService.list(queryWrapper));
    }

    @PostMapping("/sys/update")
    public R<?> updateOtaManage(@RequestBody OtaManage otaManage) {
        otaManage.setUpdateTime(Instant.now().getEpochSecond());
        LambdaQueryWrapper<OtaManage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OtaManage::getOtaId, otaManage.getOtaId());
        return R.ok(otaManageService.update(otaManage, lambdaQueryWrapper));
    }

    @PostMapping("/sys/delete")
    public R<?> deleteOtaManage(@RequestBody OtaManage otaManage) {
        LambdaQueryWrapper<OtaManage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(OtaManage::getOtaId, otaManage.getOtaId());
        return R.ok(otaManageService.remove(lambdaQueryWrapper));
    }

    @PostMapping("/sys/save")
    public R<?> saveOtaManage(@RequestBody OtaManage otaManage) {
        otaManage.setCreateTime(Instant.now().getEpochSecond());
        otaManage.setUpdateTime(Instant.now().getEpochSecond());
        return R.ok(otaManageService.save(otaManage));
    }


}
