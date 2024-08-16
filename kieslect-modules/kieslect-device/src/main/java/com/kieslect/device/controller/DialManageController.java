package com.kieslect.device.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.DeviceManage;
import com.kieslect.device.domain.DialManage;
import com.kieslect.device.domain.vo.DialManageVO;
import com.kieslect.device.domain.vo.DialVO;
import com.kieslect.device.service.IDeviceManageService;
import com.kieslect.device.service.IDialManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-08-09
 */
@RestController
@RequestMapping("/dial")
public class DialManageController {

    @Autowired
    private IDialManageService dialManageService;

    @Autowired
    private IDeviceManageService deviceManageService;

    @GetMapping("/getList")
    public R<?> getDialList(@RequestParam(value = "innerId") Integer deviceInnerId) {
        // 查询设备信息
        DeviceManage deviceManage = deviceManageService.getById(deviceInnerId);

        // 查询表盘信息
        LambdaQueryWrapper<DialManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DialManage::getDeviceInnerId, deviceInnerId);
        List<DialManage> dialList = dialManageService.list(queryWrapper);

        // 组装数据
        DialVO dialVO = new DialVO();
        BeanUtil.copyProperties(deviceManage, dialVO);
        List<DialManageVO> dialManageVOList = BeanUtil.copyToList(dialList, DialManageVO.class);
        dialManageVOList.forEach(dialManageVO -> {dialManageVO.setRelationId(deviceInnerId);dialManageVO.setDownloadCount(Integer.valueOf(RandomUtil.randomNumbers(6)));});
        dialVO.setDialList(dialManageVOList);

        Map<String, Object> result = new HashMap<>();
        result.put("dialVO", dialVO);
        return R.ok(result);
    }
}
