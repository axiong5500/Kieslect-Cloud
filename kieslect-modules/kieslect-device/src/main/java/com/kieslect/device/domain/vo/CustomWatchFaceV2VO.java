package com.kieslect.device.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class CustomWatchFaceV2VO {

    // 背景图片
    private List<CustomBackgroundVO> background;

    // 构造方法
    public CustomWatchFaceV2VO(List<CustomBackgroundVO> background) {
        this.background = background;
    }

}
