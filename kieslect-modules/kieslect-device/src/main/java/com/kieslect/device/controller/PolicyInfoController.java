package com.kieslect.device.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.PolicyInfo;
import com.kieslect.device.service.IPolicyInfoService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * <p>
 * 策略信息 前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-11-14
 */
@RestController
@RequestMapping("/policyInfo")
public class PolicyInfoController {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PolicyInfoController.class);

    @Autowired
    IPolicyInfoService policyInfoService;

    @GetMapping("/sys/getList")
    private R<?> sysGetPolicyInfoList(){
        return R.ok(policyInfoService.list());
    }

    @PostMapping("/sys/update")
    public R<?> updatePolicyInfo(@RequestBody PolicyInfo policyInfo) {
        policyInfo.setUpdateTime(Instant.now().getEpochSecond());
        return R.ok(policyInfoService.updateById(policyInfo));
    }


    @PostMapping("/sys/delete")
    public R<?> deletePolicyInfo(@RequestBody PolicyInfo policyInfo) {
        return R.ok(policyInfoService.removeById(policyInfo.getId()));
    }


    @PostMapping("/sys/save")
    public R<?> savePolicyInfo(@RequestBody PolicyInfo policyInfo) {
        policyInfo.setCreateTime(Instant.now().getEpochSecond());
        policyInfo.setUpdateTime(Instant.now().getEpochSecond());
        return R.ok(policyInfoService.save(policyInfo));
    }

}
