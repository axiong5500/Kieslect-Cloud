package com.kieslect.api.domain;

import lombok.Data;

/**
 * 用户登录对象
 * 
 * @author kieslect
 */
@Data
public class ThirdLoginInfo
{
    /**
     * 第三方授权码
     */
    private String thirdToken;

    /**
     * 第三方授权类型，0：google，1：facebook，2：apple，3：wechat
     */
    private int thirdTokenType;
    /**
     * app名字，0：Kieslect Fashion
     */
    private int appName;

    /**
     * 第三方平台用户ID，用于标识用户在第三方平台的唯一性。
     */
    private String thirdId;

    /**
     * 用户的电子邮件地址，用于联系用户或作为用户账户的标识。
     */
    private String email;

    /**
     * 是否经过验证的用户电子邮件
     */
    private String verifiedEmail;

    /**
     * 用户的全名。
     */
    private String name;

    /**
     * 用户的名，用于个人称呼。
     */
    private String firstName;

    /**
     * 用户的姓，用于家族称呼。
     */
    private String lastName;

    /**
     * 用户的中间名，某些文化中常见。
     */
    private String middleName;

    /**
     * 用户的生日，用于个性化服务或年龄验证。
     */
    private String birthday;

    /**
     * 用户的性别，用于个性化服务。
     */
    private String gender;

    /**
     * 用户的头像URL，用于显示用户图像。
     */
    private String pictureUrl;

    /**
     * 用户的个人主页链接，用于查看更多用户信息。
     */
    private String profileLink;

    /**
     * 用户的地区设置，用于显示地区特定的内容或服务。
     */
    private String locale;

    /**
     * 用户所在的时区，用于显示时间相关的信息。
     */
    private Integer timezone;

    /**
     * 第三方用户信息的最后更新时间，用于同步数据。
     */
    private String thirdUpdatedTime;


}
