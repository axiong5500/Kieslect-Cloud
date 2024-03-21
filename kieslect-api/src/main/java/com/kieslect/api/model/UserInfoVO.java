package com.kieslect.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class UserInfoVO implements Serializable {
    private Long id;
    private String account;
    private String email;
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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

}
