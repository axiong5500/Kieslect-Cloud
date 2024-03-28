package com.kieslect.weather.service.impl;

import com.kieslect.common.core.constant.CacheConstants;
import com.kieslect.common.redis.service.RedisService;
import com.kieslect.weather.service.IWeatherService;
import com.kieslect.weather.utils.GeoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherServiceImpl implements IWeatherService {

    private String apiKey = "05fbfc023e754bb2ab407fcff438b5e3";

    private static final String GEO_KEY = CacheConstants.LOCATION_KEY; // Redis 中存储经纬度的键名

    @Autowired
    private RedisService redisService;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 将经纬度存储到 Redis 中
    public void storeLocation(double latitude, double longitude, String locationName) {
        redisTemplate.opsForGeo().add(GEO_KEY, new Point(longitude, latitude), locationName);
    }

    // 查询附近的经纬度
    public List<GeoResult<RedisGeoCommands.GeoLocation<String>>> queryNearbyLocations(double latitude, double longitude, double radius) {
        Circle circle = new Circle(new Point(longitude, latitude), new Distance(radius, Metrics.KILOMETERS));
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoResults = redisTemplate.opsForGeo().radius(GEO_KEY, circle);
        return geoResults.getContent();
    }

    // 查询天气信息
    public String getWeatherInfo(double latitude, double longitude) {
        // 在这里实现获取天气信息的逻辑
        // 返回天气信息字符串
        return "Weather information for latitude: " + latitude + ", longitude: " + longitude;
    }

    // 从 Redis 中获取地理位置数据
    public Point getPoint(String locationName) {
        GeoOperations<String, String> geoOps = redisTemplate.opsForGeo();
        List<Point> points = geoOps.position(GEO_KEY, locationName);
        return points.isEmpty() ? null : points.get(0);
    }

    // 主要方法：根据传入的经纬度查询天气信息
    @Override
    public String getWeatherInfoWithCache(double latitude, double longitude) {
        // 先从缓存中查询附近的经纬度
        List<GeoResult<RedisGeoCommands.GeoLocation<String>>> nearbyLocations = queryNearbyLocations(latitude, longitude, 3); // 查询3公里范围内的经纬度

        // 如果缓存中有附近的经纬度
        if (!nearbyLocations.isEmpty()) {
            // 遍历附近的经纬度，检查是否存在满足条件的经纬度
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoResult : nearbyLocations) {
                RedisGeoCommands.GeoLocation<String> location = geoResult.getContent();

                // 打印 location 对象
                System.out.println("Location: " + location);
                Point point = getPoint(location.getName());
                // 打印 Point 对象
                System.out.println("Point: " + point);
//                Point point = location.getPoint();
                if (point != null) {
                    double locLatitude = point.getY();
                    double locLongitude = point.getX();

                    // 打印 locLatitude 和 locLongitude
                    System.out.println("Latitude: " + locLatitude + ", Longitude: " + locLongitude);


                    // 计算当前经纬度与缓存中经纬度的距离
                    double distance = GeoUtils.distance(latitude, longitude, locLatitude, locLongitude, Metrics.KILOMETERS);

                    // 打印距离
                    System.out.println("Distance: " + distance);

                    // 如果距离小于等于3公里，则表示当前经纬度在缓存中，直接返回缓存的天气信息
                    if (distance <= 3) {
                        return "Cached weather information.";
                    }
                }
            }
        }

        // 缓存中没有满足条件的经纬度，直接请求天气接口并更新缓存
        String weatherInfo = getWeatherInfo(latitude, longitude);
        storeLocation(latitude, longitude, "beijing"); // 更新缓存
        return weatherInfo;
    }

}
