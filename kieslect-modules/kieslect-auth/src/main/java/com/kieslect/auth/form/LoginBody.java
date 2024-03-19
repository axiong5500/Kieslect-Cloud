package com.kieslect.auth.form;

import lombok.Data;

/**
 * 用户登录对象
 * 
 * @author kieslect
 */
@Data
public class LoginBody
{
    /**
     * 用户名
     */
    private String account;

    /**
     * 用户密码
     */
    private String password;

}
