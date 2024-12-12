package com.kieslect.file.domain.vo;

import lombok.Data;

@Data
public class StravaCreateActivitiesVO {

    private String name;


    private String sport_type;


    private String start_date_local;


    private int elapsed_time;


    private String description;


    private int distance;


    private boolean trainer;


    private boolean commute;


    private int calories;
}
