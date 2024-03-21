package com.kieslect.auth.controller;


import cn.hutool.core.util.RandomUtil;
import com.kieslect.api.RemoteUserService;
import com.kieslect.api.domain.LoginInfo;
import com.kieslect.api.domain.RegisterInfo;
import com.kieslect.api.enums.RegisterTypeEnum;
import com.kieslect.api.model.UserInfoVO;
import com.kieslect.auth.form.RegisterBody;
import com.kieslect.auth.form.SendCaptchaBody;
import com.kieslect.auth.service.MailService;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.core.enums.ResponseCodeEnum;
import com.kieslect.common.core.utils.EmailUtils;
import com.kieslect.common.core.utils.JwtUtils;
import com.kieslect.common.core.utils.StringUtils;
import com.kieslect.common.security.auth.AuthUtil;
import com.kieslect.common.security.model.LoginUserInfo;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping("/test")
    public R<?> test() {
        return R.ok();
    }

    @PostMapping("/upload")
    public R<?> binaryUpload(@RequestParam("file") MultipartFile file) {
        return R.ok();
    }

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

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request) {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志

        }
        return R.ok();
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
     * 用户注册，0，邮箱注册，1，账号注册，2，第三方登录（没有密码）
     *
     * @param registerBody
     * @return
     */
    @PostMapping("register")
    public R<?> register(@RequestBody RegisterBody registerBody) {
        // 邮箱验证码校验
        if (registerBody.getRegisterType() == RegisterTypeEnum.EMAIL.getCode()) {
            boolean validCode = mailService.isCaptchaValid(registerBody.getAccount(), registerBody.getCode());

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

    @PostMapping("sendCaptcha")
    public R<?> sendCaptcha(@RequestBody SendCaptchaBody vo) {
        // 生成一个6位数字的验证码
        String verificationCode = RandomUtil.randomNumbers(6);
        mailService.sendVerificationCode(vo.getToEmail(), verificationCode);
        return R.ok(verificationCode);
    }


}
