package com.kieslect.user.enums;

public enum IssueTypeEnum {
    // '功能异常', '意见与建议', '其他'
    FUNCTION_ABNORMAL(0, "功能异常"),
    SUGGESTION(1, "意见与建议"),
    OTHER(2, "其他");

    private final int code;
    private final String message;

    IssueTypeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static IssueTypeEnum getEnumByCode(int code) {
        for (IssueTypeEnum issueTypeEnum : IssueTypeEnum.values()) {
            if (issueTypeEnum.getCode() == code) {
                return issueTypeEnum;
            }
        }
        return IssueTypeEnum.OTHER;
    }
    public static IssueTypeEnum getEnumByMessage(String message) {
        for (IssueTypeEnum issueTypeEnum : IssueTypeEnum.values()) {
            if (issueTypeEnum.getMessage().equals(message)) {
                return issueTypeEnum;
            }
        }
        return IssueTypeEnum.OTHER;
    }
}
