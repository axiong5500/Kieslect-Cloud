package com.kieslect.user.enums;

public enum FileTypeEnum {
    // 1代表PathTypeEnum.HEALTH_DATA的值 ,2代表PathTypeEnum.SPORT_DATA的值，请给出对应关系，我希望你能给我一个对应的关系 PathTypeEnum这是另外一个微服务的枚举
    HEALTH_DATA(1,6, "健康数据"),
    SPORT_DATA(2,8, "运动数据"),
    OTHER(9999,9999, "其他");
    ;


    private final int code;
    private final int pathTypeCode;
    private final String description;
    FileTypeEnum(int code,int pathTypeCode, String description) {
        this.code = code;
        this.pathTypeCode = pathTypeCode;
        this.description = description;
    }
    public int getCode() {
        return code;
    }
    public int getPathTypeCode() {
        return pathTypeCode;
    }
    public String getDescription() {
        return description;
    }
    public static FileTypeEnum getPathTypeCodeByCode(int code) {
        for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
            if (fileTypeEnum.getCode() == code) {
                return fileTypeEnum;
            }
        }
        return FileTypeEnum.OTHER;
    }

    public static FileTypeEnum getCodeByPathTypeCode(int pathTypeCode) {
        for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
            if (fileTypeEnum.getPathTypeCode() == pathTypeCode) {
                return fileTypeEnum;
            }
        }
        return FileTypeEnum.OTHER;
    }



}
