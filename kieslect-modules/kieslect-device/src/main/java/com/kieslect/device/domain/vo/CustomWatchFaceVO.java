package com.kieslect.device.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class CustomWatchFaceVO {
    // 字体默认颜色
    private int color;
    // 字体背景图
    private String fontUrl;
    // 背景图片
    private String backgroundUrl;
    // 宽高
    @JsonIgnore
    private int width;
    // 高度
    @JsonIgnore
    private int height;
    // 0：圆形，1：方形
    private int watchType;

    public CustomWatchFaceVO(int i0,String s0, String s1, int i, int i1, int i2) {
        this.color = i0;
        this.fontUrl = s0;
        this.backgroundUrl = s1;
        this.width = i;
        this.height = i1;
        this.watchType = i2;
    }
}
