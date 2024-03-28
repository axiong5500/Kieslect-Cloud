package com.kieslect.auth.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用户注册对象
 * 
 * @author kieslect
 */
@Data
public class RegisterBody
{
    /**
     * 用户名
     */
    @NotBlank
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
     * 验证码
     */
    private String code;

    /**
     * 注册来源类型 0:邮箱注册 1:账号注册 2:第三方授权注册
     */
    private int registerType;
}
