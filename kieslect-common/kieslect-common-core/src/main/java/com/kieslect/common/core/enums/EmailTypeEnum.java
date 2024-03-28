package com.kieslect.common.core.enums;

public enum EmailTypeEnum {
    REGISTER("Kieslect Verification Code", "Your verification code is: ", "register:"),
    FORGOT_PASSWORD("Password Reset Verification", "Your password reset verification code is: ", "forgot_password:"),
    CHANGE_PASSWORD("Password Change Verification", "Your password change verification code is: ", "change_password:"),
    CHANGE_EMAIL("Email Change Verification", "Your email change verification code is: ", "change_email:"),
    CHANGE_EMAIL_OLD("Email Change Verification", "Your email change verification code is: ", "change_email:old:"),
    CHANGE_EMAIL_NEW("Email Change Verification", "Your email change verification code is: ", "change_email:new:"),
    // 绑定邮箱
    BIND_EMAIL("Email Binding Verification", "Your email binding verification code is: ", "bind_email:"),
    // 解绑邮箱
    UNBIND_EMAIL("Email Unbinding Verification", "Your email unbinding verification code is: ", "unbind_email:"),
    // 绑定手机
    BIND_PHONE("Phone Binding Verification", "Your phone binding verification code is: ", "bind_phone:"),
    // 解绑手机
    UNBIND_PHONE("Phone Unbinding Verification", "Your phoneunbinding verification code is: ", "unbind_phone:");

    private final String emailSubject;
    private final String emailContent;
    private final String redisKey;

    private static final String MAIL_CODE_KEY = "mail:code:";

    EmailTypeEnum(String emailSubject, String emailContent, String redisKey) {
        this.emailSubject = emailSubject;
        this.emailContent = emailContent;
        this.redisKey = redisKey;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public String getEmailContent() {
        return emailContent;
    }

    public String getRedisKey() {
        return MAIL_CODE_KEY + redisKey;
    }

    // 根据 emailType 值返回对应的 EmailTypeEnum 枚举值
    public static EmailTypeEnum getEmailType(String emailType) {
        switch (emailType) {
            case "0":
                return REGISTER;
            case "1":
                return FORGOT_PASSWORD;
            case "2":
                return CHANGE_PASSWORD;
            case "3":
                return CHANGE_EMAIL;
            case "4":
                return BIND_EMAIL;
            case "5":
                return UNBIND_EMAIL;
            case "6":
                return CHANGE_EMAIL_OLD;
            case "7":
                return CHANGE_EMAIL_NEW;
            case "8":
                return BIND_PHONE;
            case "9":
                return UNBIND_PHONE;
            default:
                throw new IllegalArgumentException("No such email type: " + emailType);
        }
    }
}
