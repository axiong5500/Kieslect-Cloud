package com.kieslect.device.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class DeviceBindingUpdateVO {
    private List<DeviceBindingVO> deviceBindingList;
}
