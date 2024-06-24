package com.kieslect.user.enums;

public enum KAppNotificationTypeEnum {
    CALL(0, "com.android.server.telecom", 0x00, "Call", "UTE_CALL", "/kieslect-file/file/download/other/9999/call_icon.png"),
    QQ(1, "com.tencent.mobileqq,com.tencent.mobileqqi,com.tencent.qqlite", 0x01, "QQ", "UTE_QQ", "/kieslect-file/file/download/other/9999/qq_icon.png"),
    WECHAT(2, "com.tencent.mm", 0x02, "WeChat", "UTE_WECHAT", "/kieslect-file/file/download/other/9999/wechat_icon.png"),
    SMS(3, "kieslect_sms", 0x03, "SMS", "UTE_SMS", "/kieslect-file/file/download/other/9999/sms_icon.png"),
    OTHERS(4, "kieslect_other", 0x04, "Other", "UTE_OTHERS", "/kieslect-file/file/download/other/9999/other_icon.png"),
    Calendar(4, "kieslect_calendar", 0x04, "Calendar", "UTE_CALENDAR", "/kieslect-file/file/download/other/9999/calendar_icon.png"),
    MissCall(4, "kieslect_miss_call", 0x04, "MissCall", "UTE_MISSCAL", "/kieslect-file/file/download/other/9999/misscall_icon.png"),
    Email(4, "kieslect_email", 0x04, "Email", "UTE_EMAIL", "/kieslect-file/file/download/other/9999/email_icon.png"),
    FACEBOOK(5, "com.facebook.katana", 0x05, "Facebook", "UTE_FACEBOOK", "/kieslect-file/file/download/other/9999/facebook_icon.png"),
    TWITTER(6, "com.twitter.android", 0x06, "Twitter", "UTE_TWITTER", "/kieslect-file/file/download/other/9999/twitter_icon.png"),
    WHATSAPP(7, "com.whatsapp", 0x07, "WhatsApp", "UTE_WHATSAPP", "/kieslect-file/file/download/other/9999/whatsapp_icon.png"),
    SKYPE(8, "com.skype.raider", 0x08, "Skype", "UTE_SKYPE", "/kieslect-file/file/download/other/9999/skype_icon.png"),
    FACEBOOK_MESSENGER(9, "com.facebook.orca", 0x09, "Facebook Messenger", "UTE_FACEBOOK_MESSENGER", "/kieslect-file/file/download/other/9999/facebook_messenger_icon.png"),
    HANGOUTS(10, "com.google.android.talk", 0x0A, "Hangouts", "UTE_HANGOUTS", "/kieslect-file/file/download/other/9999/hangouts_icon.png"),
    LINE(11, "jp.naver.line.android", 0x0B, "Line", "UTE_LINE", "/kieslect-file/file/download/other/9999/line_icon.png"),
    LINKEDIN(12, "com.linkedin.android", 0x0C, "LinkedIn", "UTE_LINKEDIN", "/kieslect-file/file/download/other/9999/linkedin_icon.png"),
    INSTAGRAM(13, "com.instagram.android", 0x0D, "Instagram", "UTE_INSTAGRAM", "/kieslect-file/file/download/other/9999/instagram_icon.png"),
    VIBER(14, "com.viber.voip", 0X0E, "Viber", "UTE_VIBER", "/kieslect-file/file/download/other/9999/viber_icon.png"),
    KAKAO_TALK(15, "com.kakao.talk", 0x0F, "KakaoTalk", "UTE_KAKAO_TALK", "/kieslect-file/file/download/other/9999/kakao_talk_icon.png"),
    VKONTAKTE(16, "com.vkontakte.android", 0x10, "VKontakte", "UTE_VKONTAKTE", "/kieslect-file/file/download/other/9999/vkontakte_icon.png"),
    SNAPCHAT(17, "com.snapchat.android", 0x11, "Snapchat", "UTE_SNAPCHAT", "/kieslect-file/file/download/other/9999/snapchat_icon.png"),
    GOOGLE_PLUS(18, "com.google.android.apps.plus", 0x12, "Google Plus", "UTE_GOOGLE_PLUS", "/kieslect-file/file/download/other/9999/google_plus_icon.png"),
    GMAIL(19, "com.google.android.gm", 0x13, "Gmail", "UTE_GMAIL", "/kieslect-file/file/download/other/9999/gmail_icon.png"),
    FLICKR(20, "com.yahoo.mobile.client.android.flickr", 0x14, "Flickr", "UTE_FLICKR", "/kieslect-file/file/download/other/9999/flickr_icon.png"),
    TUMBLR(21, "com.tumblr", 0x15, "Tumblr", "UTE_TUMBLR", "/kieslect-file/file/download/other/9999/tumblr_icon.png"),
    PINTEREST(22, "com.pinterest", 0x16, "Pinterest", "UTE_PINTEREST", "/kieslect-file/file/download/other/9999/pinterest_icon.png"),
    YOUTUBE(23, "com.google.android.youtube", 0x17, "YouTube", "UTE_YOUTUBE", "/kieslect-file/file/download/other/9999/youtube_icon.png"),
    TELEGRAM(24, "org.telegram.messenger", 0x18, "Telegram", "UTE_TELEGRAM", "/kieslect-file/file/download/other/9999/telegram_icon.png"),
    TRUECALLER(25, "com.truecaller", 0x19, "Truecaller", "UTE_TRUECALLER", "/kieslect-file/file/download/other/9999/truecaller_icon.png"),
    PAYTM(26, "net.one97.paytm", 0x1A, "Paytm", "UTE_PAYTM", "/kieslect-file/file/download/other/9999/paytm_icon.png"),
    ZALO(27, "com.zing.zalo", 0x1B, "Zalo", "UTE_ZALO", "/kieslect-file/file/download/other/9999/zalo_icon.png"),
    IMO(28, "com.imo.android.imoim", 0x1C, "Imo", "UTE_IMO", "/kieslect-file/file/download/other/9999/imo_icon.png"),
    MICROSOFT_TEAMS(29, "com.microsoft.teams", 0x1D, "Microsoft Teams", "UTE_MICROSOFT_TEAMS", "/kieslect-file/file/download/other/9999/microsoft_teams_icon.png"),
    OUTLOOK(30, "com.microsoft.office.outlook", 0x1e, "Outlook", "UTE_OUTLOOK", "/kieslect-file/file/download/other/9999/outlook_icon.png"),
    SWIGGY(31, "in.swiggy.android", 0x1f, "Swiggy", "UTE_SWIGGY", "/kieslect-file/file/download/other/9999/swiggy_icon.png"),
    ZOMATO(32, "com.application.zomato", 0x20, "Zomato", "UTE_ZOMATO", "/kieslect-file/file/download/other/9999/zomato_icon.png"),
    GPAY(33, "com.google.android.apps.nbu.paisa.user", 0x21, "Gpay", "UTE_GPAY", "/kieslect-file/file/download/other/9999/gpay_icon.png"),
    PHONEPE(34, "com.phonepe.app", 0x22, "PhonePe", "UTE_PHONEPE", "/kieslect-file/file/download/other/9999/phonepe_icon.png"),
    HOTSTAR(35, "in.startv.hotstar", 0x23, "Hotstar", "UTE_HOTSTAR", "/kieslect-file/file/download/other/9999/hotstar_icon.png"),
    PRIMEVIDEO(36, "com.amazon.avod.thirdpartyclient", 0x24, "PrimeVideo", "UTE_PRIMEVIDEO", "/kieslect-file/file/download/other/9999/primevideo_icon.png"),
    FLIPKART(37, "com.flipkart.android", 0x25, "Flipkart", "UTE_FLIPKART", "/kieslect-file/file/download/other/9999/flipkart_icon.png"),
    AMAZON(38, "in.amazon.mShop.android.shopping", 0x26, "Amazon", "UTE_AMAZON", "/kieslect-file/file/download/other/9999/amazon_icon.png"),
    MYNTRA(39, "com.myntra.android", 0x27, "Myntra", "UTE_MYNTRA", "/kieslect-file/file/download/other/9999/myntra_icon.png"),
    NOISEAPP(40, "com.noisefit.app", 0x28, "NoiseApp", "UTE_NOISEAPP", "/kieslect-file/file/download/other/9999/noiseapp_icon.png"),
    DAILYHUNT(41, "com.eterno", 0x29, "DailyHunt", "UTE_DAILYHUNT", "/kieslect-file/file/download/other/9999/dailyhunt_icon.png"),
    INSHORTS(42, "com.nis.app", 0x2a, "Inshorts", "UTE_INSHORTS", "/kieslect-file/file/download/other/9999/inshorts_icon.png"),
    BOOKMYSHOW(43, "com.bt.bms", 0x2b, "BookMyShow", "UTE_BOOKMYSHOW", "/kieslect-file/file/download/other/9999/bookmyshow_icon.png"),
    CALENDAR(44, "com.google.android.calendar", 0x2c, "Calendar", "UTE_CALENDAR", "/kieslect-file/file/download/other/9999/calendar_icon.png"),
    JIO_TV(45, "com.jio.jioplay.tv", 0x2d, "Jio Tv", "UTE_JIO_TV", "/kieslect-file/file/download/other/9999/jio_tv_icon.png"),
    MAKE_MY_TRIP(46, "com.makemytrip", 0x2e, "Make My Trip", "UTE_MAKE_MY_TRIP", "/kieslect-file/file/download/other/9999/makemytrip_icon.png"),
    NETFLIX(47, "com.netflix.mediaclient", 0x2f, "Netflix", "UTE_NETFLIX", "/kieslect-file/file/download/other/9999/netflix_icon.png"),
    OLA(48, "com.olacabs.customer", 0x30, "Ola", "UTE_OLA", "/kieslect-file/file/download/other/9999/ola_icon.png"),
    REFLEX_APP(49, "com.reflexisinc.store", 0x31, "Reflex App", "UTE_REFLEX_APP", "/kieslect-file/file/download/other/9999/reflex_app_icon.png"),
    UBER(50, "com.ubercab", 0x32, "Uber", "UTE_UBER", "/kieslect-file/file/download/other/9999/uber_icon.png"),
    YT_MUSIC(51, "com.google.android.apps.youtube.music", 0x33, "YT Music", "UTE_YT_MUSIC", "/kieslect-file/file/download/other/9999/yt_music_icon.png"),
    WHATSAPP_BUSINESS(52, "com.whatsapp.w4b", 0x34, "WhatsApp Business", "UTE_WHATSAPP_BUSINESS", "/kieslect-file/file/download/other/9999/whatsapp_business_icon.png"),
    DUNZO(53, "com.dunzo.user", 0x35, "Dunzo", "UTE_DUNZO", "/kieslect-file/file/download/other/9999/dunzo_icon.png"),
    GAANA(54, "com.gaana", 0x36, "Gaana", "UTE_GAANA", "/kieslect-file/file/download/other/9999/gaana_icon.png"),
    GOOGLE_DRIVE(55, "com.google.android.apps.docs", 0x37, "Google Drive", "UTE_GOOGLE_DRIVE", "/kieslect-file/file/download/other/9999/google_drive_icon.png"),
    GOOGLECHAT(56, "com.google.android.apps.chat", 0x38, "Google Chat", "UTE_GOOGLECHAT", "/kieslect-file/file/download/other/9999/googlechat_icon.png"),
    WYNK_MUSIC(57, "com.bsbportal.music", 0x39, "Wynk music", "UTE_WYNK_MUSIC", "/kieslect-file/file/download/other/9999/wynk_music_icon.png"),
    YAHOO(58, "com.yahoo.mobile.client.android.mail", 0x3A, "Yahoo", "UTE_YAHOO", "/kieslect-file/file/download/other/9999/yahoo_icon.png"),
    TITAN_SMART_WORLD(59, "com.titan.smartworld", 0x3B, "Titan Smart World", "UTE_TITAN_SMART_WORLD", "/kieslect-file/file/download/other/9999/titan_smartworld_icon.png"),
    SLACK(60, "com.Slack", 0x3C, "Slack", "UTE_SLACK", "/kieslect-file/file/download/other/9999/slack_icon.png"),
    SPOTIFY(61, "com.spotify.music", 0x3D, "Spotify", "UTE_SPOTIFY", "/kieslect-file/file/download/other/9999/spotify_icon.png"),
    ;

    private final int code;
    private final String savePackName;

    private final String desc;
    private final String paramName;
    private final String icon;
    private final int value;


    KAppNotificationTypeEnum(int code, String savePackName, int value, String desc, String paramName, String icon) {
        this.code = code;
        this.savePackName = savePackName;
        this.desc = desc;
        this.paramName = paramName;
        this.icon = icon;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getSavePackName() {
        return savePackName;
    }

    public String getDesc() {
        return desc;
    }

    public String getParamName() {
        return paramName;
    }

    public String getIcon() {
        return icon;
    }

    public int getValue() {
        return value;
    }


}
