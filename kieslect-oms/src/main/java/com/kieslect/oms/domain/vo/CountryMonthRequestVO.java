package com.kieslect.oms.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class CountryMonthRequestVO {
    private String currentYear;
    private String countryCode;
    private String category;
    private String yearAndMonth;
    // 动态月份列表，与yearAndMonth保持一对关系
    private List<String> months;
    private List<String> prevMonths;
}
