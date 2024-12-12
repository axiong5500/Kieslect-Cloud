package com.kieslect.file.utils;

import java.util.List;

public class DistanceCalculator {
    // 地球半径，单位：公里
    private static final double EARTH_RADIUS = 6371.0;

    // 将角度转换为弧度
    private static double toRadians(double degree) {
        return degree * Math.PI / 180.0;
    }

    // 计算两点之间的距离
    private static double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double dLat = toRadians(lat2 - lat1);
        double dLng = toRadians(lng2 - lng1);
        double rLat1 = toRadians(lat1);
        double rLat2 = toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(rLat1) * Math.cos(rLat2)
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    // 计算经纬度集合的总距离
    public static double calculateTotalDistance(List<double[]> coordinates) {
        if (coordinates == null || coordinates.size() < 2) {
            return 0.0; // 少于两点无法计算距离
        }

        double totalDistance = 0.0;
        for (int i = 1; i < coordinates.size(); i++) {
            double[] point1 = coordinates.get(i - 1);
            double[] point2 = coordinates.get(i);
            totalDistance += calculateDistance(point1[0], point1[1], point2[0], point2[1]);
        }

        return totalDistance;
    }

    public static void main(String[] args) {
        // 示例：经纬度集合
        List<double[]> coordinates = List.of(
                new double[]{39.913818, 116.363625}, // 北京
                new double[]{31.230391, 121.473701}, // 上海
                new double[]{22.543099, 114.057868}  // 深圳
        );

        double totalDistance = calculateTotalDistance(coordinates);
        System.out.printf("总距离：%.2f 公里%n", totalDistance);
    }
}
