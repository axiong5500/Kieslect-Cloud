package com.kieslect.weather.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WeatherServiceImpl implements IWeatherService {

    private static final String apiKey = "ba0fb55d9b5c49ab8b352808ead4dac5";

    // 城市搜索列表
    private static final String CITY_SEARCH_URL = "https://geoapi.qweather.com/v2/city/lookup?key=" + apiKey + "&location=%s&lang=%s";

    // 实况天气
    private static final String NOW_WEATHER_URL = "https://devapi.qweather.com/v7/weather/now?key=" + apiKey + "&location=%s,%s&lang=%s&unit=%s";
    // 逐小时预报（未来24小时）
    private static final String HOURLY_WEATHER_FORECAST_URL = "https://devapi.qweather.com/v7/weather/24h?key=" + apiKey + "&location=%s,%s&lang=%s&unit=%s";

    // 7天预报
    private static final String SEVEN_WEATHER_FORECAST_URL = "https://devapi.qweather.com/v7/weather/7d?key=" + apiKey + "&location=%s,%s&lang=%s&unit=%s";

    private static final String GEO_KEY = CacheConstants.LOCATION_KEY; // Redis 中存储经纬度的键名

    private static final String WEATHER_PREFIX = "weather:";

    // 实况天气redis key前缀
    private static final String NOW_WEATHER_KEY_PREFIX = "now_weather_forecast:";

    // 逐小时redis key前缀
    private static final String HOURLY_WEATHER_FORECAST_KEY_PREFIX = "hourly_weather_forecast:";

    // 7天预报redis key前缀
    private static final String SEVEN_WEATHER_FORECAST_KEY_PREFIX = "seven_weather_forecast:";

    @Autowired
    private RedisService redisService;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public Object getWeatherInfo(int id, double latitude, double longitude, String lang, String unit) {
        Map<String, Object> weatherInfo = new HashMap<>();
        // 打印日志
        System.out.println("id:" + id + " latitude:" + latitude + " longitude:" + longitude + " lang:" + lang + " unit:" + unit);
        String latitudeFormatted = String.format("%.2f", latitude);
        String longitudeFormatted = String.format("%.2f", longitude);
        // lang 为空和null时，默认为中文
        lang = lang == null ? "" : lang;
        // unit 为空和null时，默认为公制
        unit = unit == null ? "" : unit;
        String langFormatted = lang;
        String unitFormatted = unit;
        Object hourlyObj;
        Object sevenObj;
        Object nowObj;

        // 构建 Redis 键（key）
        String redisKey = buildRedisKey(id, latitude, longitude);
        String nowRedisKey = getWeatherRedisKey(NOW_WEATHER_KEY_PREFIX, redisKey);
        String hourlyRedisKey = getWeatherRedisKey(HOURLY_WEATHER_FORECAST_KEY_PREFIX, redisKey);
        String sevenRedisKey = getWeatherRedisKey(SEVEN_WEATHER_FORECAST_KEY_PREFIX, redisKey);
        nowObj = getObject(latitudeFormatted, longitudeFormatted, langFormatted, unitFormatted, nowRedisKey, NOW_WEATHER_URL);
        hourlyObj = getObject(latitudeFormatted, longitudeFormatted, langFormatted, unitFormatted, hourlyRedisKey, HOURLY_WEATHER_FORECAST_URL);
        sevenObj = getObject(latitudeFormatted, longitudeFormatted, langFormatted, unitFormatted, sevenRedisKey, SEVEN_WEATHER_FORECAST_URL);
        weatherInfo.put("now", nowObj);
        weatherInfo.put("hourly", hourlyObj);
        weatherInfo.put("seven", sevenObj);
        return weatherInfo;
    }

    private static String getWeatherRedisKey(String prefix, String redisKey) {
        return WEATHER_PREFIX + prefix + redisKey;
    }

    private Object getObject(String latitudeFormatted, String longitudeFormatted, String langFormatted, String unitFormatted, String redisKey, String weatherForecastUrl) {
        Object weatherObj;
        if (redisService.hasKey(redisKey)) {
            weatherObj = redisService.getCacheObject(redisKey);
            System.out.println("redis缓存中获取数据" + redisKey + ",obj:" + weatherObj);
            // 处理可能包含@type字段的情况
            weatherObj = removeTypeField(weatherObj);
        } else {
            String hourlyWeatherForecast = String.format(weatherForecastUrl, longitudeFormatted, latitudeFormatted, langFormatted, unitFormatted);
            String getWeatherForecast = HttpUtil.get(hourlyWeatherForecast);
            System.out.println(getWeatherForecast);
            weatherObj = parseJson(getWeatherForecast);
            if (redisKey.contains(NOW_WEATHER_KEY_PREFIX)){
                redisService.setCacheObject(redisKey, weatherObj, 1L, TimeUnit.HOURS);
            }else if (redisKey.contains(HOURLY_WEATHER_FORECAST_KEY_PREFIX)) {
                redisService.setCacheObject(redisKey, weatherObj, 24L, TimeUnit.HOURS);
            } else if (redisKey.contains(SEVEN_WEATHER_FORECAST_KEY_PREFIX)) {
                redisService.setCacheObject(redisKey, weatherObj, 24L, TimeUnit.HOURS);
            }
        }
        return weatherObj;
    }

    private static Object removeTypeField(Object weatherObj) {
        if (weatherObj != null) {
            JSONArray jsonArr = JSONUtil.parseArray(weatherObj);
            JSONArray updatedJsonArr = new JSONArray();
            for (int i = 0; i < jsonArr.size(); i++) {
                JSONObject jsonObject = jsonArr.getJSONObject(i);
                jsonObject.remove("@type"); // 移除@type字段
                updatedJsonArr.add(jsonObject);
            }
            weatherObj = updatedJsonArr;
        }
        return weatherObj;
    }

    @Override
    public Object getCity(String location, String lang, String unit) {
        // lang 为空和null时，默认为中文
        lang = lang == null ? "zh" : lang;

        String citySearchUrl = String.format(CITY_SEARCH_URL, location, lang);
        String result = HttpUtil.get(citySearchUrl);
        Map<String, Object> weatherInfo = new HashMap<>();
        weatherInfo.put("location", parseJson(result));
        return weatherInfo;
    }

    private static String buildRedisKey(int id, double latitude, double longitude) {
        // 将 id、latitude、longitude 拼接成字符串作为 Redis 键
        return String.format("%d_%f_%f", id, latitude, longitude);
    }


    // 解析json
    public Object parseJson(String json) {
        JSONObject resultJson = JSONUtil.parseObj(json);
        if (resultJson.get("code").toString().equals("200")) {
            // 判断不为空，返回
            if (resultJson.get("now") != null) {
                return resultJson.get("now");
            }
            if (resultJson.get("daily") != null) {
                return resultJson.get("daily");
            }
            if (resultJson.get("location") != null) {
                return resultJson.get("location");
            }
            if (resultJson.get("hourly") != null) {
                return resultJson.get("hourly");
            }
        }
        return null;
    }


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
    public String requestThreeWeather(double latitude, double longitude) {
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
        String weatherInfo = requestThreeWeather(latitude, longitude);
        storeLocation(latitude, longitude, "beijing"); // 更新缓存
        return weatherInfo;
    }


}
