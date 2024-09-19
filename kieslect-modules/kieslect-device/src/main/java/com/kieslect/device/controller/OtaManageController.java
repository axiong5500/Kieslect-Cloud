package com.kieslect.device.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.OtaManage;
import com.kieslect.device.domain.vo.OtaManageVO;
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
        result.put("list", BeanUtil.copyToList(list, OtaManageVO.class));
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
        //规范化版本号
        otaManage.setOtaVersion(normalizeVersion(otaManage.getOtaVersion()));
        //规范化SortID
        otaManage.setSortId(convertSorId(otaManage.getOtaVersion()));
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
        //规范化版本号
        otaManage.setOtaVersion(normalizeVersion(otaManage.getOtaVersion()));
        //规范化SortID
        otaManage.setSortId(convertSorId(otaManage.getOtaVersion()));
        otaManage.setCreateTime(Instant.now().getEpochSecond());
        otaManage.setUpdateTime(Instant.now().getEpochSecond());
        return R.ok(otaManageService.save(otaManage));
    }

    /**
     * 规范化版本号
     *
     * @param version 版本号
     * @return 规范化后的版本号
     */
    private String normalizeVersion(String version) {
        if (version != null) {
            version = version.toUpperCase().replace("V", "");
        }
        return version;
    }


    /**
     * 将版本号转换为9位数
     * @param version
     * @return
     */
    public String convertSorId(String version) {
        String normalizeVersion = normalizeVersion(version);
        // 拆分版本号部分
        String[] parts = normalizeVersion.split("\\.");

        StringBuilder sb = new StringBuilder();

        // 将每部分补全到3位数
        for (String part : parts) {
            sb.append(String.format("%03d", Integer.parseInt(part)));
        }

        // 返回最终的9位数版本号
        return sb.toString();
    }
}
