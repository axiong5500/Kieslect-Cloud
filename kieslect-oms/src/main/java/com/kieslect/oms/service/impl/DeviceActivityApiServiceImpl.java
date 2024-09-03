package com.kieslect.oms.service.impl;

import com.kieslect.oms.domain.vo.DeviceActivityApiQueryVO;
import com.kieslect.oms.mapper.DeviceActivityApiMapper;
import com.kieslect.oms.service.IDeviceActivityApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DeviceActivityApiServiceImpl implements IDeviceActivityApiService {

    @Autowired
    private DeviceActivityApiMapper deviceActivityApiMapper;

    @Override
    public List<Map<String, Object>> getList(DeviceActivityApiQueryVO queryVO) {
        // 使用 Optional 提取 startDate 和 endDate，提供默认值 null
        Long startDate = Optional.ofNullable(queryVO)
                .map(DeviceActivityApiQueryVO::getStartDate)
                .orElse(null);
        Long endDate = Optional.ofNullable(queryVO)
                .map(DeviceActivityApiQueryVO::getEndDate)
                .orElse(null);
        return deviceActivityApiMapper.getList(startDate, endDate);
    }
}
