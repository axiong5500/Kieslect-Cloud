package com.kieslect.api.domain;

import lombok.Data;

@Data
public class RegisterInfo {

    /**
     * 注册类型
     */
    private int registerType;

    /**
     * 用户名
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;
    /**
     * 应用名称
     */
    private Byte appName;

    /**
     * 用户第三方鉴权token
     */
    private String thirdPartyToken;

    /**
     * 用户第三方鉴权token类型
     */
    private int thirdTokenType;

    /**
     * 用户注册ip
     */
    private String ipAddress;
}
