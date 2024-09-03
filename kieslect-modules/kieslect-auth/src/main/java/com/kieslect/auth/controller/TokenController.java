package com.kieslect.auth.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.kieslect.api.RemoteUserService;
import com.kieslect.api.domain.*;
import com.kieslect.api.enums.RegisterTypeEnum;
import com.kieslect.api.model.UserInfoVO;
import com.kieslect.auth.form.RegisterBody;
import com.kieslect.auth.form.SendCaptchaBody;
import com.kieslect.auth.utils.ValidationUtils;
import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.core.enums.CaptchaEmailTypeEnum;
import com.kieslect.common.core.enums.EmailTypeEnum;
import com.kieslect.common.core.enums.ResponseCodeEnum;
import com.kieslect.common.core.ip.IpUtils;
import com.kieslect.common.core.utils.JwtUtils;
import com.kieslect.common.core.utils.StringUtils;
import com.kieslect.common.mail.service.MailService;
import com.kieslect.common.redis.service.RedisService;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * token 控制
 *
 * @author kieslect
 */
@RestController
public class TokenController  {

    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(TokenController.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private MailService mailService;

    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private RedisService redisService;

    /**
     * 登录
     *
     * @param
     * @return
     */
    @PostMapping("third/login")
    public R<?> thirdLogin(@RequestBody ThirdLoginInfo thirdLoginInfo,HttpServletRequest request) {
        String clientIp = IpUtils.getIpAddr(request);
        return remoteUserService.thirdLogin(thirdLoginInfo,clientIp);
    }

    /**
     * 登录
     *
     * @param loginInfo
     * @return
     */
    @PostMapping("login")
    public R<?> login(@RequestBody @Valid LoginInfo loginInfo) {
        // 用户登录
        String userKey = IdUtil.fastUUID();
        loginInfo.setUserKey(userKey);
        R<UserInfoVO> result = remoteUserService.login(loginInfo);
        if (R.isError(result)) {
            // 登录失败，处理错误信息...
            return result;
        }
        UserInfoVO userInfo = result.getData();
        LoginUserInfo loginUserInfo = new LoginUserInfo();
        BeanUtil.copyProperties(userInfo, loginUserInfo, CopyOptions.create().setFieldMapping(
                Map.of("id", "kid")
        ));
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
    public R<?> register(@RequestBody @Valid RegisterBody registerBody,HttpServletRequest request) {
        String clientIp = IpUtils.getIpAddr(request);
        logger.info("客户端IP:{}",clientIp);
        // 邮箱验证码校验
        if (registerBody.getRegisterType() == RegisterTypeEnum.EMAIL.getCode()) {
            String email = EmailTypeEnum.REGISTER.getRedisKey() + registerBody.getAccount();
            boolean validCode = mailService.isCaptchaValid(email, registerBody.getCode());

            if (!validCode) {
                return R.fail(ResponseCodeEnum.CAPTCHA_ERROR);
            }

            // 删除验证码
            redisService.deleteObject(email);
        }

        // 账号注册校验是否包含@邮箱格式
        if (registerBody.getRegisterType() == RegisterTypeEnum.ACCOUNT.getCode()) {
            if (ValidationUtils.isValidEmail(registerBody.getAccount())) {
                return R.fail(ResponseCodeEnum.ACCOUNT_FORMAT_ERROR);
            }
        }

        // 注册用户信息
        RegisterInfo registerInfo = new RegisterInfo();
        BeanUtils.copyProperties(registerBody, registerInfo);
        R<Boolean> result = remoteUserService.registerUserInfo(registerInfo,clientIp);
        if (R.isError(result)) {
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
        //校验邮箱格式
        if (!ValidationUtils.isValidEmail(vo.getToEmail())) {
            return R.fail(ResponseCodeEnum.EMAIL_FORMAT_ERROR);
        }

        //发送邮箱验证码之前校验是否已经注册过
        R<Boolean> result = remoteUserService.isEmailExists(vo.getToEmail(), vo.getAppName());
        if (vo.getEmailType().equals(CaptchaEmailTypeEnum.REGISTER.getCode())) {
            if (result.getData()) {
                return R.fail(ResponseCodeEnum.EMAIL_ALREADY_EXIST);
            }
        }
        if (vo.getEmailType().equals(CaptchaEmailTypeEnum.FORGET_PASSWORD.getCode())) {
            if (!result.getData()) {
                return R.fail(ResponseCodeEnum.EMAIL_NOT_EXIST);
            }
        }
//        if (vo.getEmailType().equals(CaptchaEmailTypeEnum.CHANGE_EMAIL.getCode())) {
//            if (result.getData()) {
//                return R.fail(ResponseCodeEnum.EMAIL_ALREADY_EXIST);
//            }
//        }
        if (vo.getEmailType().equals(CaptchaEmailTypeEnum.BIND_EMAIL.getCode())) {
            if (result.getData()) {
                return R.fail(ResponseCodeEnum.EMAIL_ALREADY_EXIST);
            }
        }
//        if (vo.getEmailType().equals(CaptchaEmailTypeEnum.CHANGE_EMAIL_NEW.getCode())) {
//            if (result.getData()) {
//                return R.fail(ResponseCodeEnum.EMAIL_ALREADY_EXIST);
//            }
//        }

        // 生成一个6位数字的验证码
        String verificationCode = RandomUtil.randomNumbers(6);
        mailService.sendVerificationCode(vo.getToEmail(), vo.getEmailType(), verificationCode);
        return R.ok();
    }

    @PostMapping("getUserInfo")
    public R<?> getUserInfo(HttpServletRequest request) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        UserInfoVO userInfoVO = new UserInfoVO();
        if (StringUtils.isNotNull(loginUser)) {
            BeanUtils.copyProperties(loginUser, userInfoVO);
        }
        // 获取第三方用户信息
        userInfoVO.setThirdUserInfos(remoteUserService.getThirdUserInfo(loginUser.getKid()).getData());
        return R.ok(userInfoVO);
    }

    @PostMapping("forgetPassword")
    public R<?> forgetPassword(@RequestBody @Valid ForgetPasswordBody body) {
        String email = EmailTypeEnum.FORGOT_PASSWORD.getRedisKey() + body.getAccount();
        boolean validCode = mailService.isCaptchaValid(email, body.getCode());

        if (!validCode) {
            return R.fail(ResponseCodeEnum.CAPTCHA_ERROR);
        }

        // 验证码正确情况下删除验证码
        redisService.deleteObject(email);

        return remoteUserService.forgetPassword(body);
    }

    // 账号注销分两种情况，一种是带有token格式，一种是只有账号密码没有token
    @PostMapping("logout")
    public R<?> logout(HttpServletRequest request, @RequestBody(required = false) LogoutBody logoutBody) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String userKey = JwtUtils.getUserKey(token);
            Long userId = JwtUtils.getUserId(token);
            // 删除用户缓存记录
            tokenService.delLoginUserByUserkey(userKey);
            remoteUserService.logout(userId);
            return R.ok();
        } else {
            // 判断账号和密码是否正确
            R<UserInfoVO> result = remoteUserService.logoutByAccountAndPassword(logoutBody);
            UserInfoVO userInfoVO = result.getData();
            if (userInfoVO == null) {
                return result;
            }
            // 删除用户缓存记录
            tokenService.delLoginUserByUserkey(userInfoVO.getUserKey());
            return R.ok();
        }
    }

    @PostMapping("gettest")
    public R<?> test() {
        return R.ok();
    }

}
