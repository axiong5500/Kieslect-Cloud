package com.kieslect.device.enums;

public enum AppManageAppNameEnum {
    //0：KSTYLEOS，其他：KSTYLEOS
    KSTYLEOS(0, "KSTYLEOS"),
    KSTYLEOS_OTHER(1, "KSTYLEOS")
    ;

    private final int code;
    private final String appMark;

    public static AppManageAppNameEnum getEnumByCode(int code) {
        for (AppManageAppNameEnum appManageAppNameEnum : AppManageAppNameEnum.values()) {
            if (appManageAppNameEnum.getCode() == code) {
                return appManageAppNameEnum;
            }
        }
        return AppManageAppNameEnum.KSTYLEOS_OTHER;
    }

    public static AppManageAppNameEnum getEnumByAppMark(String appMark) {
        for (AppManageAppNameEnum appManageAppNameEnum : AppManageAppNameEnum.values()) {
            if (appManageAppNameEnum.getAppMark().equals(appMark)) {
                return appManageAppNameEnum;
            }
        }
        return AppManageAppNameEnum.KSTYLEOS_OTHER;
    }


    AppManageAppNameEnum(int code, String appMark) {
        this.code = code;
        this.appMark = appMark;
    }

    public int getCode() {
        return code;
    }

    public String getAppMark() {
        return appMark;
    }
}
