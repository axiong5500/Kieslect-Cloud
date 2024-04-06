package com.kieslect.api;


import com.kieslect.api.domain.ForgetPasswordBody;
import com.kieslect.api.domain.LoginInfo;
import com.kieslect.api.domain.LogoutBody;
import com.kieslect.api.domain.RegisterInfo;
import com.kieslect.api.model.UserInfoVO;
import com.kieslect.common.core.constant.ServiceNameConstants;
import com.kieslect.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 用户服务
 * 
 * @author kieslect
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.USER_SERVICE)
public interface RemoteUserService
{
    /**
     * 通过用户名查询用户信息
     *
     * @param username 用户名
     * @return 结果
     */
    @GetMapping("/user/info/{username}")
    R<UserInfoVO> getUserInfo(@PathVariable("username") String username);

    /**
     * 注册用户信息
     *
     * @return 结果
     */
    @PostMapping(value = "/user/register")
    R<Boolean> registerUserInfo(@RequestBody RegisterInfo registerInfo);

    /**
     * 登录
     *
     * @param loginInfo 登录信息
     * @return 结果
     */
    @PostMapping(value = "/user/login")
    R<UserInfoVO> login(@RequestBody LoginInfo loginInfo);

    /**
     * 邮箱是否存在
     * @param toEmail
     * @param appName
     * @return
     */
    @GetMapping(value = "/user/isEmailExists/{toEmail}/{appName}")
    R<Boolean> isEmailExists(@PathVariable("toEmail") String toEmail,@PathVariable("appName") Byte appName);

    @PostMapping(value = "/user/forgetPassword")
    R<Object> forgetPassword(@RequestBody ForgetPasswordBody body);

    @PostMapping(value = "/user/logout/{userId}")
    void logout(@PathVariable("userId") Long userId);


    @PostMapping(value = "/user/logoutByAccountAndPassword")
    R<UserInfoVO> logoutByAccountAndPassword(@RequestBody LogoutBody logoutBody);
}