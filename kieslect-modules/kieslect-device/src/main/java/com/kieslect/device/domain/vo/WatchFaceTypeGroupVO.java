package com.kieslect.device.domain.vo;

import lombok.Data;

@Data
public class WatchFaceTypeGroupVO {
    int watchFaceTypeId;
    String watchFaceTypeName;
    int watchFaceTypeStatus;

    public WatchFaceTypeGroupVO(int s0, String s1, int s2) {
        this.watchFaceTypeId = s0;
        this.watchFaceTypeName = s1;
        this.watchFaceTypeStatus = s2;
    }
}
