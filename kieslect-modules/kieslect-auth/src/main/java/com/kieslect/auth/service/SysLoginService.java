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
    public LoginUser login(String account, String password)
    {
//        if (EmailUtils.isEmail(account))
//        {
//            // 邮箱登录
//            // 查找邮箱在不在数据库
//            return null;
//        }
//        else
//        {
//            // 账号登录
//            return null;
//        }
        LoginUser loginUser =  new LoginUser();
        loginUser.setUserid(1L);
        loginUser.setUsername(account);
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
