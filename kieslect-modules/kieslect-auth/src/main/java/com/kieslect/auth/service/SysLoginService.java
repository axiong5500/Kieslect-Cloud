package com.kieslect.auth.service;


import com.kieslect.common.security.model.LoginUser;
import org.springframework.stereotype.Component;

/**
 * 登录校验方法
 * 
 * @author kieslect
 */
@Component
public class SysLoginService
{

    /**
     * 登录
     */
    public LoginUser login(String username, String password)
    {
        LoginUser loginUser =  new LoginUser();
        loginUser.setUserid(1L);
        loginUser.setUsername(username);
        return loginUser;
    }

    public void logout(String loginName)
    {

    }

    /**
     * 注册
     */
    public void register(String username, String password)
    {

    }
}
