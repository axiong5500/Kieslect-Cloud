package com.kieslect.file.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor  // 生成带所有字段的构造方法
public class ActivityVO {
    private String sportType;
    private String activityId;
    private String lapStartTime;
    // 单位是卡路里
    private int calories;
    private List<TrackpointDataVO> trackpointDataList;
}
