package com.kieslect.user.domain.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Getter
@Setter
@Accessors(chain = true)
public class SaveUserInfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    // 账号
    private String account;
    // 邮箱
    private String email;
    // 密码
    private String password;
    // 第三方token
    private String thirdToken;
    // 第三方token类型,0：google，1：facebook，2：apple，3：wechat
    private Byte thirdTokenType;
    // 性别，0：男，1：女，2：其它
    private Byte sex;
    // 性别，0：男，1：女，2：其它
    private Byte newSex;
    // 生日
    private Long birthday;
    // 身高
    private Double height;
    // 体重
    private Double weight;
    // 国家，废弃
    @Deprecated
    private String country;
    // 省份，废弃
    @Deprecated
    private String province;
    // 城市，废弃
    @Deprecated
    private String city;
    // 城市id
    private Integer cityId;
    // gps城市id
    private Integer gpsCityId;
    // 昵称
    private String nickName;
    // 头像
    private String headImage;
    // app名称，0：kieslect
    private Byte appName;
    // app系统名称，0：android，1：ios
    private Byte appSystem;
    // 手机类型
    private String phoneType;
    // app渠道下载来源
    private Integer appChannel;
    // app包环境，0：release，1：debug
    private Byte appStatus;
    // app版本
    private String appVersion;
    // 系统版本
    private String systemVersion;
    // 0：公制，1：英制
    private Byte metricBritish;
    // 0：24小时制，1：12小时制
    private Byte hourly;
    // 0：摄氏度，1：华氏度
    private Byte temperature;
    // 更新时间
    private Long updateTime;
    // 目标
    private int stepsAim;
    // 活动目标
    private int activityAim;
    // 距离目标
    private double distanceAim;
    // 卡路里目标
    private double caloriesAim;
    // 睡眠目标
    private int sleepAim;
    // 体重目标
    private double weightAim;
}
