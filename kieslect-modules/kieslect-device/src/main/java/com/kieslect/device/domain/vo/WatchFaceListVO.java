package com.kieslect.device.domain.vo;

import lombok.Data;

@Data
public class WatchFaceListVO {
    private int watchFaceId;
    private String watchFaceName;
    private String watchFaceUrl;
    private int watchFaceType;
    private String watchFaceDesc;

    private int watchFaceStatus;

    private String otaFaceLinkUrl;
    private String otaFaceLinkMd5;
    private int otaFaceLinkSize;

    public WatchFaceListVO(int number, String s, String s0, int s1, String s2, int s3, String s4, String s5, int s6) {
        this.watchFaceId = number;
        this.watchFaceName = s;
        this.watchFaceUrl = s0;
        this.watchFaceType = s1;
        this.watchFaceDesc = s2;
        this.watchFaceStatus = s3;
        this.otaFaceLinkUrl = s4;
        this.otaFaceLinkMd5 = s5;
        this.otaFaceLinkSize = s6;
    }
}
