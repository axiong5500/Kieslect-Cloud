package com.kieslect.user.enums;

public enum KAppNotificationTypeEnum {
    SMS(0, "sms"),
    Email(1, "email"),
    Calendar(2, "calendar"),
    MissCall(3, "miss_call"),
    QQ1(4, "com.tencent.mobileqq"),
    QQ2(4, "com.tencent.mobileqqi"),
    QQ3(4, "com.tencent.qqlite"),
    weChat(5, "com.tencent.mm"),
    whatsApp(6, "com.whatsapp"),
    facebook(7, "com.facebook.katana"),
    line(8, "jp.naver.line.android"),
    twitter(9, "com.twitter.android"),
    skype(10, "com.skype.raider"),
    instagram(11, "com.instagram.android"),
    gmail(12, "com.google.android.gm"),
    linkedin(13, "com.linkedin.android"),
    snapchat(14, "com.snapchat.android"),
    kakaotalk(15, "com.kakao.talk"),
    viber(16, "com.viber.voip"),
    kik(17, "kik.android"),
    DingTalk(18, "com.alibaba.android.rimet"),
    Other(999, "Other");

    private final int code;
    private final String savePackName;

    KAppNotificationTypeEnum(int code, String savePackName) {
        this.code = code;
        this.savePackName = savePackName;
    }

    public int getCode() {
        return code;
    }

    public String getSavePackName() {
        return savePackName;
    }
}
