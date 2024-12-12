package com.kieslect.file.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor  // 生成带所有字段的构造方法
public class TrackpointDataVO {
    // 时间
    private String time;
    // 纬度
    private double latitude;
    // 经度
    private double longitude;
    // 海拔，单位：米
    private double altitude;
    // 距离，单位：米
    private double distance;
    // 心率，单位：bpm
    private int heartRate;
    // 步频，单位：步/分钟
    private int cadence;
}
