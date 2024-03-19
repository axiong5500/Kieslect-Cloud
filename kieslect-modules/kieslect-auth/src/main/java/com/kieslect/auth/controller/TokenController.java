package com.kieslect.auth.controller;


import cn.hutool.core.util.RandomUtil;
import com.kieslect.auth.form.LoginBody;
import com.kieslect.auth.form.RegisterBody;
import com.kieslect.auth.service.MailService;
import com.kieslect.auth.service.SysLoginService;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.core.utils.JwtUtils;
import com.kieslect.common.core.utils.StringUtils;
import com.kieslect.common.security.auth.AuthUtil;
import com.kieslect.common.security.model.LoginUser;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * token 控制
 * 
 * @author kieslect
 */
@RestController
public class TokenController
{
    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysLoginService sysLoginService;

    @Autowired
    private MailService mailService;

    @GetMapping("/test")
    public R<?> test()
    {
        return R.ok();
    }

    @PostMapping("/upload")
    public R<?> binaryUpload(@RequestParam("file") MultipartFile file)
    {
        return R.ok();
    }

    @PostMapping("login")
    public R<?> login(@RequestBody LoginBody form)
    {
        // 用户登录
        LoginUser userInfo = sysLoginService.login(form.getAccount(), form.getPassword());
        // 获取登录token
        return R.ok(tokenService.createToken(userInfo));
    }

    @DeleteMapping("logout")
    public R<?> logout(HttpServletRequest request)
    {
        String token = SecurityUtils.getToken(request);
        if (StringUtils.isNotEmpty(token))
        {
            String username = JwtUtils.getUserName(token);
            // 删除用户缓存记录
            AuthUtil.logoutByToken(token);
            // 记录用户退出日志

        }
        return R.ok();
    }

    @PostMapping("refresh")
    public R<?> refresh(HttpServletRequest request)
    {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser))
        {
            // 刷新令牌有效期
            tokenService.refreshToken(loginUser);
            return R.ok();
        }
        return R.ok();
    }

    @PostMapping("register")
    public R<?> register(@RequestBody RegisterBody registerBody)
    {
        boolean validCode = mailService.isCaptchaValid(registerBody.getAccount(), registerBody.getCode());

        if (!validCode)
        {
            return R.fail("验证码错误");
        }

        // 用户注册
        sysLoginService.register(registerBody.getAccount(), registerBody.getPassword());
        return R.ok();
    }

    @PostMapping("sendCaptcha")
    public R<?> sendCaptcha(@RequestBody String toEmail)
    {
        // 生成一个6位数字的验证码
        String verificationCode = RandomUtil.randomNumbers(6);
        mailService.sendVerificationCode(toEmail,verificationCode);
        return R.ok(verificationCode);
    }


}
