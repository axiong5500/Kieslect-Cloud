package com.kieslect.api.enums;

public enum RegisterTypeEnum {
    EMAIL(0, "邮箱"),
    ACCOUNT(1, "账号"),
    THIRD_PARTY_AUTH(2, "第三方登录授权登录")
    ;


    private final int code;
    private final String description;

    RegisterTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static RegisterTypeEnum getByCode(int code) {
        for (RegisterTypeEnum type : RegisterTypeEnum.values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid Register Type code: " + code);
    }
}
