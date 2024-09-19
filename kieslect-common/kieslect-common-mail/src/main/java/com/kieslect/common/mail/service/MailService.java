package com.kieslect.common.mail.service;

import com.kieslect.common.core.config.MailConfig;
import com.kieslect.common.core.enums.EmailTypeEnum;
import com.kieslect.common.core.utils.EmailUtils;
import com.kieslect.common.redis.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MailService {

    private static final long MAIL_TIMEOUT_KEY =  60 * 60L; // 60分钟有效期

    @Autowired
    private MailConfig mailConfig;

    @Autowired
    private RedisService redisService;

    public void sendVerificationCode(String to, String emailType, String code,Byte appName) {
        String emailSubject = EmailTypeEnum.getEmailType(emailType).getEmailSubject();
        String emailContent = EmailTypeEnum.getEmailType(emailType).getEmailContent();
        String redisKey = EmailTypeEnum.getEmailType(emailType).getRedisKey();
        String content = emailContent + code;

        // 根据 appName 选择相应的邮件账号配置
        MailConfig.MailAccountConfig accountConfig = mailConfig.getAccounts().get(String.valueOf(appName));
        if (accountConfig == null) {
            throw new IllegalArgumentException("未找到对应的邮件配置");
        }

        // 使用选定的邮件账号发送邮件
        EmailUtils.sendEmail(accountConfig,to, emailSubject, content);

        // 将验证码存储到 Redis 缓存中
        redisService.setCacheObject(redisKey + to, code, MAIL_TIMEOUT_KEY, TimeUnit.SECONDS);
    }

    public boolean isCaptchaValid(String email, String code){
        // 从缓存中获取验证码
        String storedCode = redisService.getCacheObject(email);

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