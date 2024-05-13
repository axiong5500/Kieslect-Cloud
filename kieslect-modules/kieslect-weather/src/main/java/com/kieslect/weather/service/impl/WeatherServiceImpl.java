package com.kieslect.weather.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kieslect.common.redis.service.RedisService;
import com.kieslect.weather.domain.vo.WeatherInfoVO;
import com.kieslect.weather.service.IWeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    private static final String HEFENG_PREFIX = "hefeng:";

    private static final String VO_PREFIX = "vo:";


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
        return getHefengWeatherInfo(id, latitude, longitude, lang, unit);
    }

    private Map<String, Object> getHefengWeatherInfo(int id, double latitude, double longitude, String lang, String unit) {
        Map<String, Object> weatherInfo = new HashMap<>();
        // 打印日志
        logger.info("id:" + id + " latitude:" + latitude + " longitude:" + longitude + " lang:" + lang + " unit:" + unit);
        String latitudeFormatted = String.format("%.2f", latitude);
        String longitudeFormatted = String.format("%.2f", longitude);
        // lang 为空和null时，默认为中文
        String langFormatted = StrUtil.isEmpty(lang) ? "zh" : lang;
        // unit 为空和null时，默认为公制
        String unitFormatted = StrUtil.isEmpty(unit) ? "m" : unit;
        Object hourlyObj;
        Object sevenObj;
        Object nowObj;


        // 构建 Redis 键（key）
        String redisKey = buildRedisKey(id, latitude, longitude, langFormatted, unitFormatted);
        String redisKeyVO = getWeatherRedisKey(VO_PREFIX, redisKey);
        if (ObjectUtil.isNotNull(redisService.getCacheObject(redisKeyVO))) {
            weatherInfo.put("weatherInfo", redisService.getCacheObject(redisKeyVO));
            return weatherInfo;
        }


        String nowRedisKey = getWeatherRedisKey(NOW_WEATHER_KEY_PREFIX, redisKey);
        String hourlyRedisKey = getWeatherRedisKey(HOURLY_WEATHER_FORECAST_KEY_PREFIX, redisKey);
        String sevenRedisKey = getWeatherRedisKey(SEVEN_WEATHER_FORECAST_KEY_PREFIX, redisKey);
        nowObj = getObject(latitudeFormatted, longitudeFormatted, langFormatted, unitFormatted, nowRedisKey, NOW_WEATHER_URL);
        hourlyObj = getObject(latitudeFormatted, longitudeFormatted, langFormatted, unitFormatted, hourlyRedisKey, HOURLY_WEATHER_FORECAST_URL);
        sevenObj = getObject(latitudeFormatted, longitudeFormatted, langFormatted, unitFormatted, sevenRedisKey, SEVEN_WEATHER_FORECAST_URL);

        WeatherInfoVO weatherInfoVO = new WeatherInfoVO();
        weatherInfoVO.setLang(langFormatted);
        setWeatherInfo(nowObj, weatherInfoVO, hourlyObj, sevenObj);

        //放入缓存
        redisService.setCacheObject(redisKeyVO, weatherInfoVO, 1L, TimeUnit.HOURS);

        weatherInfo.put("weatherInfo", weatherInfoVO);
        return weatherInfo;
    }

    private void setWeatherInfo(Object nowObj, WeatherInfoVO weatherInfoVO, Object hourlyObj, Object sevenObj) {
        if (ObjectUtil.isNotEmpty(nowObj)) {
            JSONObject nowObjJson = JSONUtil.parseObj(nowObj);
            weatherInfoVO.setTemperature(nowObjJson.getStr("temp"));
            weatherInfoVO.setFeelsLike(nowObjJson.getStr("feelsLike"));
            weatherInfoVO.setWeather(nowObjJson.getStr("text"));
            weatherInfoVO.setHumidity(nowObjJson.getStr("humidity"));
            weatherInfoVO.setWeatherIcon(nowObjJson.getStr("icon"));
            // 以下6个字段无法从nowObj对象中获取到，需从sevenObj对象中获取

            weatherInfoVO.setHourlyForecast(getHourlyForecast(weatherInfoVO,hourlyObj));
            weatherInfoVO.setDailyForecast(getDailyForecast(weatherInfoVO, sevenObj));
        }
    }

    private List<WeatherInfoVO.DailyVO> getDailyForecast(WeatherInfoVO weatherInfoVO, Object sevenObj) {
        if (ObjectUtil.isNotEmpty(sevenObj)) {
            JSONArray sevenObjJson = JSONUtil.parseArray(sevenObj);
            LocalDate today = LocalDate.now();
            List<WeatherInfoVO.DailyVO> dailyForecast = new ArrayList<>();
            sevenObjJson.forEach(item -> {
                JSONObject itemJson = JSONUtil.parseObj(item);
                String fxDate = itemJson.getStr("fxDate");
                LocalDate forecastDate = LocalDate.parse(fxDate.substring(0, 10));
                // 判断当前天气是否为今天
                if (forecastDate.equals(today)) {
                    weatherInfoVO.setSunrise(itemJson.getStr("sunrise"));
                    weatherInfoVO.setSunset(itemJson.getStr("sunset"));
                    weatherInfoVO.setTemperatureHigh(itemJson.getStr("tempMax"));
                    weatherInfoVO.setTemperatureLow(itemJson.getStr("tempMin"));
                    weatherInfoVO.setUv(itemJson.getStr("uvIndex"));
                    weatherInfoVO.setUvDescription(getUVDescription(itemJson.getInt("uvIndex"),weatherInfoVO.getLang()));
                }
                itemJson.set("date", DateUtil.format(DateUtil.parse(itemJson.getStr("fxDate")), "MM-dd"));
                itemJson.set("temperatureLow", itemJson.getStr("tempMin"));
                itemJson.set("temperatureHigh", itemJson.getStr("tempMax"));
                itemJson.set("weather", itemJson.getStr("textDay"));
                itemJson.set("weatherIcon", itemJson.getStr("iconDay"));
                String week;
                if (weatherInfoVO.getLang().equals("zh")){
                    week = DateUtil.dayOfWeekEnum(DateUtil.parse(itemJson.getStr("fxDate"))).toChinese();
                    // 判断是不是今天日期
                    if (forecastDate.equals(today)){
                        week = "今天";
                    }
                }else{
                    week = DateUtil.dayOfWeekEnum(DateUtil.parse(itemJson.getStr("fxDate"))).toString();
                    // 判断是不是今天日期
                    if (forecastDate.equals(today)){
                        week = "Today";
                    }
                }
                itemJson.set("week", week);
                WeatherInfoVO.DailyVO dailyVO = JSONUtil.toBean(itemJson, WeatherInfoVO.DailyVO.class);
                dailyForecast.add(dailyVO);
            });
            return dailyForecast;
        }
        return null;
    }

    private List<WeatherInfoVO.HourlyVO> getHourlyForecast(WeatherInfoVO weatherInfoVO,Object hourlyObj) {
        if (ObjectUtil.isNotEmpty(hourlyObj)) {
            JSONArray hourlyObjJson = JSONUtil.parseArray(hourlyObj);
            List<WeatherInfoVO.HourlyVO> result = new ArrayList<>();
            //把当前时间加上
            WeatherInfoVO.HourlyVO currentHourlyVO = new WeatherInfoVO.HourlyVO();
            currentHourlyVO.setTemperature(weatherInfoVO.getTemperature());
            currentHourlyVO.setWeather(weatherInfoVO.getWeather());
            currentHourlyVO.setWeatherIcon(weatherInfoVO.getWeatherIcon());
            currentHourlyVO.setTime(LocalDateTime.now().toLocalTime().toString());
            if (weatherInfoVO.getLang().equals("zh")){
                currentHourlyVO.setTime("现在");
            }else{
                currentHourlyVO.setTime("Now");
            }
            result.add(currentHourlyVO);

            OffsetDateTime now = OffsetDateTime.now();
            List<WeatherInfoVO.HourlyVO> hourlyForecast = hourlyObjJson.stream()
                    .map(item -> {
                        JSONObject itemJson = JSONUtil.parseObj(item);
                        itemJson.set("temperature", itemJson.getStr("temp"));
                        itemJson.set("weather", itemJson.getStr("text"));
                        itemJson.set("weatherIcon", itemJson.getStr("icon"));
                        String dateTimeString = itemJson.getStr("fxTime");
                        OffsetDateTime dateTime = OffsetDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);


                        // 判断时间是否是大于当前时间的下一个小时，如果不是则返回 null
                        if (dateTime.isAfter(now)) {
                            String time = dateTime.toLocalTime().toString();
                            itemJson.set("time", time);
                            return itemJson.toBean(WeatherInfoVO.HourlyVO.class);
                        } else {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull) // 过滤掉 null 元素
                    .collect(Collectors.toList());
            result.addAll(hourlyForecast);
            return result;
        }
        return null;
    }


    private Map<String, Object> getV1WeatherInfo(int id, double latitude, double longitude, String lang, String unit) {
        Map<String, Object> weatherInfo = new HashMap<>();
        // 打印日志
        logger.info("id:" + id + " latitude:" + latitude + " longitude:" + longitude + " lang:" + lang + " unit:" + unit);
        String latitudeFormatted = String.format("%.2f", latitude);
        String longitudeFormatted = String.format("%.2f", longitude);
        // lang 为空和null时，默认为中文
        String langFormatted = StrUtil.isEmpty(lang) ? "zh" : lang;
        // unit 为空和null时，默认为公制
        String unitFormatted = StrUtil.isEmpty(unit) ? "m" : unit;
        Object hourlyObj;
        Object sevenObj;
        Object nowObj;

        // 构建 Redis 键（key）
        String redisKey = buildRedisKey(id, latitude, longitude, langFormatted, unitFormatted);
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
        return WEATHER_PREFIX + getHefengRedisKey(prefix, redisKey);
    }

    private static String getHefengRedisKey(String prefix, String redisKey) {
        return HEFENG_PREFIX + prefix + redisKey;
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

    private static String buildRedisKey(int id, double latitude, double longitude, String langFormatted, String unitFormatted) {
        // 将 id、latitude、longitude 拼接成字符串作为 Redis 键
        return String.format("%d_%f_%f_%s_%s", id, latitude, longitude, langFormatted, unitFormatted);
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

    public static String getUVDescription(int uvIndex, String locale) {
        if (uvIndex < 3) {
            return locale.equals("zh") ? "低" : "Low";
        } else if (uvIndex < 6) {
            return  locale.equals("zh") ? "中等" : "Moderate";
        } else if (uvIndex < 8) {
            return locale.equals("zh") ? "强" : "Strong";
        } else if (uvIndex < 11) {
            return locale.equals("zh") ? "很强" : "Very Strong";
        } else {
            return locale.equals("zh") ? "极强" : "Extreme";
        }
    }

}
