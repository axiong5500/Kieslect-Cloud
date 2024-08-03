package com.kieslect.file.enums;

public enum PathTypeEnum {
    // 0:头像，1：健康数据，2：运动数据，3：问题反馈，4：app日志，5：后台管理，6：健康运动数据， 9999：其他 请用小写字母生成
    HEAD_IMAGE(0,"headimage", "头像"),
    HEALTH_DATA(1,"healthdata", "健康数据"),
    SPORT_DATA(2,"sportdata", "运动数据"),
    FEEDBACK(3,"feedback", "问题反馈"),
    APP_LOG(4,"applog", "app日志"),
    BACKGROUND_MANAGE(5,"backgroundmanage", "后台管理"),
    HEALTH_SPORT_DATA(6,"healthsportdata", "健康运动数据"),
    NOTICE_ICO(7,"noticeico", "通知ico"),
    SPORT_DATA_NEW(8,"sportdata", "运动数据"),
    OTA(9,"ota", "ota"),
    WATCH_FACE(10,"watchface", "表盘"),
    OTHER(9999,"other", "其他");

    private final int code;
    private final String path;
    private final String description;

    PathTypeEnum(int code, String path, String description) {
        this.code = code;
        this.path = path;
        this.description = description;
    }

    public static PathTypeEnum getByCode(int code) {
        for (PathTypeEnum pathTypeEnum : PathTypeEnum.values()) {
            if (pathTypeEnum.getCode() == code) {
                return pathTypeEnum;
            }
        }
        return PathTypeEnum.OTHER;
    }

    public static PathTypeEnum getByPath(String path) {
        for (PathTypeEnum pathTypeEnum : PathTypeEnum.values()) {
            if (pathTypeEnum.getPath().equals(path)) {
                return pathTypeEnum;
            }
        }
        return PathTypeEnum.OTHER;
    }

    public int getCode() {
        return code;
    }

    public String getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

}
