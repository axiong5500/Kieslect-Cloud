package com.kieslect.api.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户登录对象
 * 
 * @author kieslect
 */
@Data
public class LoginInfo
{
    /**
     * 用户名
     */
    private String account;

    /**
     * 用户密码
     */
    @NotBlank
    private String password;
    /**
     * app名字
     */
    @NotNull
    private Byte appName;

    /**
     * 用户唯一标识
     */
    private String userKey;

}
