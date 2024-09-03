package com.kieslect.oms.service;

import com.kieslect.oms.domain.vo.DeviceActivityApiQueryVO;

import java.util.List;
import java.util.Map;

public interface IDeviceActivityApiService {

    List<Map<String, Object>> getList(DeviceActivityApiQueryVO activationQueryVO);
}
