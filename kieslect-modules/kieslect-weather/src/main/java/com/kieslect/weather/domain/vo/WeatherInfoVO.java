package com.kieslect.weather.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * 天气信息视图对象
 */
@Data
public class WeatherInfoVO {

    /**
     * 语言
     */
    @JsonIgnore
    private String lang;

    /**
     * 当前温度
     */
    private String temperature;

    /**
     * 天气状况，如晴、雨、多云等
     */
    private String weather;

    /**
     * 天气图标代码，用于展示天气图标
     */
    private String weatherIcon;

    /**
     * 最低温度
     */
    private String temperatureLow;

    /**
     * 最高温度
     */
    private String temperatureHigh;

    /**
     * 紫外线指数
     */
    private String uv;

    /**
     * 风向360角度
     */
    private String wind360;
    /**
     * 风向
     */
    private String windDir;
    /**
     * 风速
     */
    private String windSpeed;
    /**
     * 风级
     */
    private String windScale;

    /**
     * 大气压强
     */
    private String pressure;

    /**
     * 能见度
     */
    private String visibility;
    /**
     * 体感温度
     */
    private String feelsLike;

    /**
     * 相对湿度百分比
     */
    private String humidity;
    /**
     * 日出时间
     */
    private String sunrise;
    /**
     * 日落时间
     */
    private String sunset;
    /**
     * 降水概率
     */
    private String pop;

    /**
     * 每小时天气预报列表
     */
    private List<HourlyVO> hourlyForecast;

    /**
     * 每日天气预报列表
     */
    private List<DailyVO> dailyForecast;

    /**
     * 小时天气预报视图对象
     */
    @Data
    public static class HourlyVO {

        /**
         * 小时温度
         */
        private String temperature;

        /**
         * 小时内天气状况
         */
        private String weather;

        /**
         * 小时天气图标代码
         */
        private String weatherIcon;

        /**
         * 时间点，通常为小时形式
         */
        private String time;
        /**
         * 降水概率
         */
        private String pop;
        /**
         * 大气压强
         */
        private String pressure;
    }

    /**
     * 每日天气预报视图对象
     */
    @Data
    public static class DailyVO {

        /**
         * 日期
         */
        private Long date;
        /**
         * 日最低温度
         */
        private String temperatureLow;

        /**
         * 日最高温度
         */
        private String temperatureHigh;

        /**
         * 当日天气状况
         */
        private String weather;

        /**
         * 当日天气图标代码
         */
        private String weatherIcon;

        /**
         * 日出时间
         */
        private String sunrise;

        /**
         * 日落时间
         */
        private String sunset;

        /**
         * 紫外线指数
         */
        private String uv;
        /**
         * 大气压强
         */
        private String pressure;
    }
}

