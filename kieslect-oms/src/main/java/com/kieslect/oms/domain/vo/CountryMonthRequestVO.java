package com.kieslect.oms.domain.vo;

import lombok.Data;

@Data
public class CountryMonthRequestVO {
    private String currentYear;
    private String countryCode;
    private String category;
}
