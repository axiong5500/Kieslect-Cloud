package com.kieslect.auth.controller.api;

import com.kieslect.api.domain.ForgetPasswordBody;
import com.kieslect.api.domain.LoginInfo;
import com.kieslect.api.domain.LogoutBody;
import com.kieslect.api.domain.ThirdLoginInfo;
import com.kieslect.auth.form.RegisterBody;
import com.kieslect.auth.form.SendCaptchaBody;
import com.kieslect.common.core.domain.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface TokenApi {
    /**
     * 第三方登录接口.
     *
     * @param thirdLoginInfo 第三方登录信息
     * @return 返回登录响应
     */
    R<?> thirdLogin(@RequestBody ThirdLoginInfo thirdLoginInfo);

    /**
     * 普通用户登录接口.
     *
     * @param loginInfo 登录信息
     * @return 返回登录响应
     */
    R<?> login(@RequestBody @Valid LoginInfo loginInfo);

    /**
     * 刷新登录状态接口.
     *
     * @param request HTTP请求
     * @return 返回刷新状态响应
     */
    R<?> refresh(HttpServletRequest request);

    /**
     * 用户注册接口.
     *
     * @param registerBody 注册信息
     * @return 返回注册响应
     */
    R<?> register(@RequestBody @Valid RegisterBody registerBody);

    /**
     * 发送验证码接口.
     *
     * @param sendCaptchaBody 发送验证码的信息
     * @return 返回验证码发送响应
     */
    R<?> sendCaptcha(@RequestBody @Valid SendCaptchaBody sendCaptchaBody);

    /**
     * 获取用户信息接口.
     *
     * @param request HTTP请求
     * @return 返回用户信息响应
     */
    R<?> getUserInfo(HttpServletRequest request);

    /**
     * 忘记密码接口.
     *
     * @param body 忘记密码的信息
     * @return 返回忘记密码响应
     */
    R<?> forgetPassword(@RequestBody @Valid ForgetPasswordBody body);

    /**
     * 注销登录接口.
     *
     * @param request HTTP请求
     * @param logoutBody 注销信息
     * @return 返回注销响应
     */
    R<?> logout(HttpServletRequest request, @RequestBody(required = false) LogoutBody logoutBody);

    /**
     * 测试接口.
     *
     * @return 返回测试响应
     */
    R<?> test();
}
