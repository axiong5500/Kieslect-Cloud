package com.kieslect.auth.controller;


import cn.hutool.core.util.RandomUtil;
import com.kieslect.api.RemoteUserService;
import com.kieslect.api.domain.LoginInfo;
import com.kieslect.api.domain.RegisterInfo;
import com.kieslect.api.enums.RegisterTypeEnum;
import com.kieslect.api.model.UserInfoVO;
import com.kieslect.auth.enums.EmailTypeEnum;
import com.kieslect.auth.form.ForgetPasswordBody;
import com.kieslect.auth.form.RegisterBody;
import com.kieslect.auth.form.SendCaptchaBody;
import com.kieslect.auth.service.MailService;
import com.kieslect.common.core.context.RequestContextHolder;
import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.core.enums.ResponseCodeEnum;
import com.kieslect.common.core.utils.EmailUtils;
import com.kieslect.common.core.utils.StringUtils;
import com.kieslect.common.security.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * token 控制
 *
 * @author kieslect
 */
@RestController
public class TokenController {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private MailService mailService;

    @Autowired
    private RemoteUserService remoteUserService;

    /**
     *  登录
     * @param loginInfo
     * @return
     */
    @PostMapping("login")
    public R<?> login(@RequestBody @Valid LoginInfo loginInfo) {
        // 用户登录
        R<UserInfoVO> result = remoteUserService.login(loginInfo);
        if (R.isError(result)) {
            // 登录失败，处理错误信息...
            return result;
        }
        UserInfoVO userInfo = result.getData();
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        BeanUtils.copyProperties(userInfo, loginUserInfo);
        // 登录成功，处理用户信息...
        // 获取登录token
        return R.ok(tokenService.createToken(loginUserInfo));
    }


    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    /**
     * 用户注册
     * 0，邮箱注册，1，账号注册，2，第三方登录（没有密码）
     *
     * @param registerBody
     * @return
     */
    @PostMapping("register")
    public R<?> register(@RequestBody RegisterBody registerBody) {
        // 邮箱验证码校验
        if (registerBody.getRegisterType() == RegisterTypeEnum.EMAIL.getCode()) {
            String email = EmailTypeEnum.REGISTER.getRedisKey() + registerBody.getAccount();
            boolean validCode = mailService.isCaptchaValid(email, registerBody.getCode());

            if (!validCode) {
                return R.fail(ResponseCodeEnum.CAPTCHA_ERROR);
            }
        }

        // 账号注册校验是否包含@邮箱格式
        if (registerBody.getRegisterType() == RegisterTypeEnum.ACCOUNT.getCode()) {
            if (EmailUtils.isEmail(registerBody.getAccount())) {
                return R.fail(ResponseCodeEnum.ACCOUNT_FORMAT_ERROR);
            }
        }

        // 注册用户信息
        RegisterInfo registerInfo = new RegisterInfo();
        BeanUtils.copyProperties(registerBody, registerInfo);
        R<Boolean> result = remoteUserService.registerUserInfo(registerInfo);
        if (R.isError(result)){
            return result;
        }
        return R.ok();
    }

    /**
     * 发送验证码
     *
     * @param vo
     * @return
     */
    @PostMapping("sendCaptcha")
    public R<?> sendCaptcha(@RequestBody @Valid SendCaptchaBody vo) {
        //发送邮箱验证码之前校验是否已经注册过
        R<Boolean> result = remoteUserService.isEmailExists(vo.getToEmail(),vo.getAppName());
        if (result.getData()){
            return R.fail(ResponseCodeEnum.EMAIL_ALREADY_EXIST);
        }
        // 生成一个6位数字的验证码
        String verificationCode = RandomUtil.randomNumbers(6);
        mailService.sendVerificationCode(vo.getToEmail(),vo.getEmailType(), verificationCode);
        return R.ok(verificationCode);
    }

    @PostMapping("getUserInfo")
    public R<?> getUserInfo() {
        LoginUserInfo loginUser = RequestContextHolder.getLoginUser();
        UserInfoVO userInfoVO = new UserInfoVO();
        if (StringUtils.isNotNull(loginUser)) {

            BeanUtils.copyProperties(loginUser, userInfoVO);
        }
        return R.ok(userInfoVO);
    }

    @PostMapping("forgetPassword")
    public R<?> forgetPassword(@RequestBody @Valid ForgetPasswordBody body) {

        return R.ok();
    }

    @GetMapping("test")
    public R<?> getTest() {
        return R.ok();
    }
}
