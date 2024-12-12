package com.kieslect.file.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampUtils {
    /**
     * 将 Unix 时间戳转换为 ISO 8601 格式的字符串
     * @param timestamp Unix 时间戳（秒）
     * @return 转换后的 ISO 8601 格式的日期时间字符串
     */
    public static String convertToISO8601(long timestamp) {
        // 将 Unix 时间戳转换为 Instant
        Instant instant = Instant.ofEpochSecond(timestamp);

        // 将 Instant 转换为 UTC 时区的 ZonedDateTime
        ZonedDateTime dateTime = instant.atZone(ZoneOffset.UTC);

        // 格式化为 ISO 8601 字符串
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    /**
     * 将 Unix 时间戳转换为带偏移的 ISO 8601 格式字符串
     *
     * @param timestamp  Unix 时间戳（秒）
     * @param offsetHours  时区偏移小时数（支持负值，如 -5 表示 UTC-5）
     * @return ISO 8601 格式的时间字符串
     */
    public static String convertToISO8601WithDecimalOffset(long timestamp, double offsetHours) {
        // 将小数部分转换为小时和分钟
        int hours = (int) offsetHours; // 整数部分为小时
        int minutes = (int) ((offsetHours - hours) * 60); // 小数部分转分钟

        // 将 Unix 时间戳转换为 Instant
        Instant instant = Instant.ofEpochSecond(timestamp);

        // 使用小时和分钟构造偏移时区
        ZoneOffset offset = ZoneOffset.ofHoursMinutes(hours, minutes);

        // 转换为指定偏移的 ZonedDateTime
        ZonedDateTime dateTime = instant.atZone(offset);

        // 格式化为 ISO 8601 字符串
        return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static void main(String[] args) {
        // 示例时间戳
        long timestamp = 1732242350L;

        // 调用工具方法转换时间戳
        String iso8601String = convertToISO8601(timestamp);

        // 打印结果
        System.out.println("ISO 8601 格式的时间: " + iso8601String);
    }
}
