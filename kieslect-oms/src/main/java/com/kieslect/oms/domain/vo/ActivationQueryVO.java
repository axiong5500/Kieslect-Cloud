package com.kieslect.oms.domain.vo;

import lombok.Data;

@Data
public class ActivationQueryVO {
    private String startDate; // 查询的开始日期，格式为 'YYYY-MM-DD'
    private String endDate;   // 查询的结束日期，格式为 'YYYY-MM-DD'
    private String countryCode;   // 查询的国家代码
    private String noIncludeCountryCode;   // 查询的不包含国家代码
}
