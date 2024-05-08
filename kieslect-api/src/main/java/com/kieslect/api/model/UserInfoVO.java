package com.kieslect.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class UserInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private int cityId;
    private String nickName;
    private String headImage;
    private Byte appName;
    private Byte appSystem;
    private String appType;
    private Integer appChannel;
    private Byte appStatus;
    private String appVersion;
    private Byte metricBritish;
    private Byte hourly;
    private Byte temperature;
    private Byte firstLogin;
    private byte delStatus;
    private Long createTime;
    private Long updateTime;
    private String userKey;
    private int stepsAim;
    private int activityAim;
    private double distanceAim;
    private double caloriesAim;
    private int sleepAim;
    private double weightAim;
}
