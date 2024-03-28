package com.kieslect.weather.utils;

import org.springframework.data.geo.Metrics;


public class GeoUtils {
    private static final double EARTH_RADIUS = 6371.0; // 地球半径（单位：公里）

    // 将角度转换为弧度
    private static double toRadians(double degrees) {
        return degrees * Math.PI / 180.0;
    }

    // 计算两个经纬度之间的距离（单位：公里）
    public static double distance(double lat1, double lon1, double lat2, double lon2, Metrics metrics) {
        double lat1Rad = toRadians(lat1);
        double lon1Rad = toRadians(lon1);
        double lat2Rad = toRadians(lat2);
        double lon2Rad = toRadians(lon2);

        double dlon = lon2Rad - lon1Rad;
        double dlat = lat2Rad - lat1Rad;

        double a = Math.pow(Math.sin(dlat / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 根据所选的度量单位返回距离
        double distanceInKilometers = EARTH_RADIUS * c;
        switch (metrics) {
            case KILOMETERS:
                return distanceInKilometers;
            case MILES:
                return distanceInKilometers * 0.621371;
            default:
                throw new IllegalArgumentException("Unsupported metrics");
        }
    }
}
