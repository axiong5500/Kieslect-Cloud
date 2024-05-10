package com.kieslect.weather.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kieslect.common.redis.service.RedisService;
import com.kieslect.weather.service.IWeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WeatherServiceImpl implements IWeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);
    private static final String apiKey = "ba0fb55d9b5c49ab8b352808ead4dac5";

    // 实况天气
    private static final String NOW_WEATHER_URL = "https://devapi.qweather.com/v7/weather/now?key=" + apiKey + "&location=%s,%s&lang=%s&unit=%s";
    // 逐小时预报（未来24小时）
    private static final String HOURLY_WEATHER_FORECAST_URL = "https://devapi.qweather.com/v7/weather/24h?key=" + apiKey + "&location=%s,%s&lang=%s&unit=%s";

    // 7天预报
    private static final String SEVEN_WEATHER_FORECAST_URL = "https://devapi.qweather.com/v7/weather/7d?key=" + apiKey + "&location=%s,%s&lang=%s&unit=%s";

    private static final String WEATHER_PREFIX = "weather:";

    // 实况天气redis key前缀
    private static final String NOW_WEATHER_KEY_PREFIX = "now_weather_forecast:";

    // 逐小时redis key前缀
    private static final String HOURLY_WEATHER_FORECAST_KEY_PREFIX = "hourly_weather_forecast:";

    // 7天预报redis key前缀
    private static final String SEVEN_WEATHER_FORECAST_KEY_PREFIX = "seven_weather_forecast:";

    @Autowired
    private RedisService redisService;


    @Override
    public Object getWeatherInfo(int id, double latitude, double longitude, String lang, String unit) {
        Map<String, Object> weatherInfo = new HashMap<>();
        // 打印日志
        logger.info("id:" + id + " latitude:" + latitude + " longitude:" + longitude + " lang:" + lang + " unit:" + unit);
        String latitudeFormatted = String.format("%.2f", latitude);
        String longitudeFormatted = String.format("%.2f", longitude);
        // lang 为空和null时，默认为中文
        String langFormatted = StrUtil.isEmpty(lang)  ? "zh" : lang;
        // unit 为空和null时，默认为公制
        String unitFormatted = StrUtil.isEmpty(unit) ? "m" : unit;
        Object hourlyObj;
        Object sevenObj;
        Object nowObj;

        // 构建 Redis 键（key）
        String redisKey = buildRedisKey(id, latitude, longitude,langFormatted,unitFormatted);
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
            logger.info("redis缓存中获取数据" + redisKey + ",obj:" + weatherObj);
            // 处理可能包含@type字段的情况
            if (redisKey.contains(NOW_WEATHER_KEY_PREFIX)) {
                weatherObj = removeTypeFieldByJsonObj(weatherObj);
            } else {
                weatherObj = removeTypeField(weatherObj);
            }
        } else {
            String hourlyWeatherForecast = String.format(weatherForecastUrl, longitudeFormatted, latitudeFormatted, langFormatted, unitFormatted);
            String getWeatherForecast = HttpUtil.get(hourlyWeatherForecast);
            logger.info(getWeatherForecast);
            weatherObj = parseJson(getWeatherForecast);
            if (redisKey.contains(NOW_WEATHER_KEY_PREFIX)) {
                redisService.setCacheObject(redisKey, weatherObj, 1L, TimeUnit.HOURS);
            } else if (redisKey.contains(HOURLY_WEATHER_FORECAST_KEY_PREFIX)) {
                redisService.setCacheObject(redisKey, weatherObj, 24L, TimeUnit.HOURS);
            } else if (redisKey.contains(SEVEN_WEATHER_FORECAST_KEY_PREFIX)) {
                redisService.setCacheObject(redisKey, weatherObj, 24L, TimeUnit.HOURS);
            }
        }
        return weatherObj;
    }

    private static Object removeTypeFieldByJsonObj(Object weatherObj) {
        if (weatherObj != null) {
            JSONObject jsonObject = JSONUtil.parseObj(weatherObj);
            jsonObject.remove("@type"); // 移除@type字段
            weatherObj = jsonObject;
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

    private static String buildRedisKey(int id, double latitude, double longitude,String langFormatted, String unitFormatted) {
        // 将 id、latitude、longitude 拼接成字符串作为 Redis 键
        return String.format("%d_%f_%f_%s_%s", id, latitude, longitude,langFormatted, unitFormatted);
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

}
