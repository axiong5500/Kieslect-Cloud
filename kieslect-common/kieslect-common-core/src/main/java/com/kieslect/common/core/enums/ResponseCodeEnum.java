package com.kieslect.common.core.enums;

public enum ResponseCodeEnum {
    SUCCESS(200, "成功"),
    FAIL(400, "失败"),
    UNAUTHORIZED(401, "token已失效"),

    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    CAPTCHA_ERROR(1001, "验证码错误"),
    ACCOUNT_FORMAT_ERROR(1002, "账号注册不能包含@邮箱格式"),
    INCORRECT_PASSWORD(1003, "密码错误"),
    //账号不存在
    ACCOUNT_NOT_EXIST(1004, "账号不存在"),
    ACCOUNT_ALREADY_EXIST(1005, "账号已存在"),
    //邮箱格式错误
    EMAIL_FORMAT_ERROR(1006, "邮箱格式错误"),
    //邮箱不存在
    EMAIL_NOT_EXIST(1007, "邮箱不存在"),
    EMAIL_ALREADY_EXIST(1008, "邮箱已存在"),
    // 邮箱验证码错误
    EMAIL_CAPTCHA_ERROR(1009, "邮箱验证码错误"),
    // 上传文件不为空
    FILE_NOT_EMPTY(1010, "上传文件不为空"),

    PARAM_ERROR (1000, "参数错误"),
    ;

    private final int code;
    private final String message;

    ResponseCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
