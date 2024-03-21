package com.kieslect.user.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDate birthday;
    private Double height;
    private Double weight;
    private String country;
    private String province;
    private String city;
    private String nickName;
    private String headImage;
    private String appName;
    private Byte appSystem;
    private String appType;
    private Integer appChannel;
    private Byte appStatus;
    private String appVersion;
    private Byte metricBritish;
    private Byte hourly;
    private Byte temperature;
    private Byte firstLogin;
    private Byte delStatus;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public RegisterUserInfoDTO() {
        // 设置默认初始化值
        this.account = "kieslect_" + Instant.now().toEpochMilli();
        this.password = "123456";
        this.email = "kieslect_" + Instant.now().toEpochMilli() + "@kieslect.com";
        this.sex = 0;
        this.metricBritish = 0;
        this.hourly = 0;
        this.temperature = 0;
        this.firstLogin = 0;
        this.delStatus = 0;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }
}
