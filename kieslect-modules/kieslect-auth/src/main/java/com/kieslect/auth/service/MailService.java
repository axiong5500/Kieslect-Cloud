package com.kieslect.auth.service;

import com.kieslect.common.core.config.MailConfig;
import com.kieslect.common.core.utils.EmailUtils;
import com.kieslect.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


@Service
public class MailService {

    private static final long MAIL_TIMEOUT_KEY = 60L; // 60秒有效期
    private static final String MAIL_CODE_KEY = "mail:code:"; // 60秒有效期

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private RedisService redisService;

    public void sendVerificationCode(String to, String code) {

        String subject = "Verification Code";
        String content = "Your verification code is: " + code;
        EmailUtils.sendEmail(mailConfig,to, subject, content);
        redisService.setCacheObject(MAIL_CODE_KEY + to, code, MAIL_TIMEOUT_KEY, TimeUnit.SECONDS);
    }

    public boolean isCaptchaValid(String email, String code){
        // 从缓存中获取验证码
        String storedCode = redisService.getCacheObject(MAIL_CODE_KEY + email);

        // 如果缓存中没有存储的验证码，则认为验证码无效
        if (storedCode == null) {
            return false;
        }

        // 验证码验证
        if (storedCode.equals(code)) {
            // 验证码正确
            return true;
        } else {
            // 验证码错误
            return false;
        }
    }

}
