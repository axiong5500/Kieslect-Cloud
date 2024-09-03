package com.kieslect.oms.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.oms.domain.vo.DeviceActivityApiQueryVO;
import com.kieslect.oms.service.IDeviceActivityApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oms/api")
public class DeviceActivityApiController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DeviceActivityApiController.class);

    @Autowired
    private IDeviceActivityApiService deviceActivityApiService;

    @PostMapping("/device/activity/getList")
    public R<?> getList(@RequestBody(required = false) DeviceActivityApiQueryVO deviceActivityApiQueryVO) {
        //打印参数
        logger.info("deviceActivityApiQueryVO:{}", deviceActivityApiQueryVO);
        List<Map<String, Object>> list = deviceActivityApiService.getList(deviceActivityApiQueryVO);
        return R.ok(list);
    }

}
