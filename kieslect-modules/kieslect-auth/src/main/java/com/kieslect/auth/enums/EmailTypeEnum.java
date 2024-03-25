package com.kieslect.auth.enums;

public enum EmailTypeEnum {
    REGISTER("Kieslect Verification Code", "Your verification code is: ", "register:"),
    FORGOT_PASSWORD("Password Reset Verification", "Your password reset verification code is: ", "forgot_password:"),
    CHANGE_PASSWORD("Password Change Verification", "Your password change verification code is: ", "change_password:"),
    CHANGE_EMAIL("Email Change Verification", "Your email change verification code is: ", "change_email:");

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
            default:
                throw new IllegalArgumentException("No such email type: " + emailType);
        }
    }
}
