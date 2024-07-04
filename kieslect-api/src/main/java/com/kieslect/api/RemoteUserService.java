package com.kieslect.api;


import com.kieslect.api.domain.*;
import com.kieslect.api.model.ThirdUserInfoVO;
import com.kieslect.api.model.UserInfoVO;
import com.kieslect.common.core.constant.ServiceNameConstants;
import com.kieslect.common.core.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 用户服务
 * 
 * @author kieslect
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.USER_SERVICE)
public interface RemoteUserService
{
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

    /**
     * 账号注销
     * @param userId
     */
    @PostMapping(value = "/user/logout/{userId}")
    void logout(@PathVariable("userId") Long userId);


    @PostMapping(value = "/user/logoutByAccountAndPassword")
    R<UserInfoVO> logoutByAccountAndPassword(@RequestBody LogoutBody logoutBody);

    @PostMapping(value = "/user/third/login")
    R<?> thirdLogin(@RequestBody ThirdLoginInfo thirdLoginInfo);

    @PostMapping(value = "/user/third/getThirdUserInfo/{userId}")
    R<List<ThirdUserInfoVO>> getThirdUserInfo(@PathVariable("userId") Long userId);
}
