package com.kieslect.user.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

@Getter
@Setter
@Accessors(chain = true)
public class RegisterUserInfoDTO {
    private Long id;
    private String account;
    private String email;
    private String password;
    private String thirdToken;
    private Byte thirdTokenType;
    private Byte sex;
    private Long birthday;
    private Double height;
    private Double weight;
    private String country;
    private String province;
    private String city;
    private Integer cityId;
    private String nickName;
    private String headImage;
    private Byte appName;
    private Byte appSystem;
    private String phoneType;
    private Integer appChannel;
    private Byte appStatus;
    private String appVersion;
    private Byte metricBritish;
    private Byte hourly;
    private Byte temperature;
    private Byte firstLogin;
    private Byte delStatus;
    private Long createTime;
    private Long updateTime;
    private String ipAddress;

    public RegisterUserInfoDTO() {
        // 设置默认初始化值
        this.account = "kieslect_" + Instant.now().toEpochMilli();
        this.password = "123456";
        this.sex = 0;
        this.cityId = 0;
        this.metricBritish = 0;
        this.hourly = 0;
        this.temperature = 0;
        this.firstLogin = 1;
        this.delStatus = 0;
        this.createTime =  Instant.now().getEpochSecond();
        this.updateTime = Instant.now().getEpochSecond();
    }
}
