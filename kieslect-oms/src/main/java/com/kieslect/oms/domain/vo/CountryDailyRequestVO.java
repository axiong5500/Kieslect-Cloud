package com.kieslect.oms.domain.vo;

import lombok.Data;

@Data
public class CountryDailyRequestVO extends ActivationQueryVO{
    private String category; // 查询的类别，可选值为 'activation'、'device'
}
