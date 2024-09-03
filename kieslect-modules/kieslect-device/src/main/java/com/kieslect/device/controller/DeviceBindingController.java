package com.kieslect.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.core.ip.IpUtils;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.device.domain.DeviceBinding;
import com.kieslect.device.domain.vo.DeviceBindingUpdateVO;
import com.kieslect.device.domain.vo.DeviceBindingVO;
import com.kieslect.device.service.IDeviceBindingService;
import com.kieslect.device.service.async.DeviceActivationService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-04-12
 */
@RestController
@RequestMapping("/deviceBinding")
public class DeviceBindingController {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceBindingController.class);

    @Autowired
    private IDeviceBindingService deviceBindingService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private DeviceActivationService deviceActivationService;



    @GetMapping("/getList")
    public R<Map<String, Object>> getDeviceBindingList(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        Long userid = loginUser.getKid();
        QueryWrapper<DeviceBinding> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userid);
        queryWrapper.orderByDesc("update_time");
        List<DeviceBinding> deviceBindingList = deviceBindingService.list(queryWrapper);
        List<DeviceBindingVO> deviceBindingVOList = new ArrayList<>();
        deviceBindingList.forEach(deviceBinding -> {
            DeviceBindingVO deviceBindingVO = new DeviceBindingVO();
            BeanUtils.copyProperties(deviceBinding, deviceBindingVO);
            deviceBindingVOList.add(deviceBindingVO);
        });
        map.put("deviceBindingList", deviceBindingVOList);
        return R.ok(map);
    }

    @PostMapping("/update")
    public R<?> updateDeviceBinding(HttpServletRequest request, @RequestBody DeviceBindingUpdateVO deviceBindingList) {
        List<DeviceBindingVO> DeviceBindingVos = deviceBindingList.getDeviceBindingList();
        if (DeviceBindingVos == null || DeviceBindingVos.size() == 0) {
            return R.fail("设备列表不能为空");
        }
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        Long userid = loginUser.getKid();
        deviceBindingService.remove(new QueryWrapper<DeviceBinding>().eq("user_id", userid));
        List<DeviceBinding> deviceBindings = DeviceBindingVos.stream().map(vo -> {
            DeviceBinding deviceBinding = new DeviceBinding();
            BeanUtils.copyProperties(vo, deviceBinding);
            deviceBinding.setUserId(Math.toIntExact(userid));
            deviceBinding.setId(vo.getBindingId());
            deviceBinding.setCreateTime(String.valueOf(Instant.now().getEpochSecond()));
            deviceBinding.setUpdateTime(String.valueOf(Instant.now().getEpochSecond()));
            return deviceBinding;
        }).toList();
        deviceBindingService.saveOrUpdateBatch(deviceBindings);

        String clientIp = IpUtils.getIpAddr(request);
        deviceActivationService.AsyncSaveDeviceActivationInfo(deviceBindings, loginUser, clientIp);
        return R.ok();
    }

}
