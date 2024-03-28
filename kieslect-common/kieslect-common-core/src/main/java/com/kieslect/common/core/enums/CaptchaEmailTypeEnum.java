package com.kieslect.common.core.enums;

public enum CaptchaEmailTypeEnum {
    // 0：注册，1：忘记密码，2：修改密码，3：修改邮箱
    REGISTER("0", "注册"),
    FORGET_PASSWORD("1", "忘记密码"),
    CHANGE_PASSWORD("2", "修改密码"),
    CHANGE_EMAIL("3", "修改邮箱"),
    // 绑定邮箱
    BIND_EMAIL("4", "绑定邮箱"),
    // 解绑邮箱
    UNBIND_EMAIL("5", "解绑邮箱"),
    CHANGE_EMAIL_OLD("6", "旧邮箱发送验证码"),
    CHANGE_EMAIL_NEW("7", "新邮箱发送验证码"),
    // 绑定手机
    BIND_PHONE("8", "绑定手机"),
    // 解绑手机
    UNBIND_PHONE("9", "解绑手机"),
    ;
    ;


    private final String code;
    private final String description;

    CaptchaEmailTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CaptchaEmailTypeEnum getByCode(String code) {
        for (CaptchaEmailTypeEnum type : CaptchaEmailTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid CaptchaEmail Type code: " + code);
    }
}
