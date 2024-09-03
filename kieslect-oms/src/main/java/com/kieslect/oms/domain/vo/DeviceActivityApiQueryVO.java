package com.kieslect.oms.domain.vo;

import lombok.Data;

@Data
public class DeviceActivityApiQueryVO {
    private long startDate; // 查询的开始日期，格式为时间戳
    private long endDate;   // 查询的结束日期，格式为时间戳
}
