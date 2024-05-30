package com.kieslect.weather.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kieslect.common.redis.service.RedisService;
import com.kieslect.weather.domain.Geoname;
import com.kieslect.weather.domain.GeonameThirdGeo;
import com.kieslect.weather.domain.vo.WeatherInfoVO;
import com.kieslect.weather.service.IGeonameService;
import com.kieslect.weather.service.IGeonameThirdGeoService;
import com.kieslect.weather.service.IWeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements IWeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);
    private static final String apiKey = "ba0fb55d9b5c49ab8b352808ead4dac5";

    // 实况天气 经度（longitude）在前 纬度（latitude）在后
    private static final String NOW_WEATHER_URL = "https://devapi.qweather.com/v7/weather/now?key=" + apiKey + "&location=%s,%s&lang=%s&unit=%s";
    // 逐小时预报（未来24小时） 经度（longitude）在前 纬度（latitude）在后
    private static final String HOURLY_WEATHER_FORECAST_URL = "https://devapi.qweather.com/v7/weather/24h?key=" + apiKey + "&location=%s,%s&lang=%s&unit=%s";

    // 7天预报 经度（longitude）在前 纬度（latitude）在后
    private static final String SEVEN_WEATHER_FORECAST_URL = "https://devapi.qweather.com/v7/weather/7d?key=" + apiKey + "&location=%s,%s&lang=%s&unit=%s";

    // 和风geo
    private static final String HEFENG_GEO_URL = "https://geoapi.qweather.com/v2/city/lookup?key=" + apiKey + "&location=%s&number=1";


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

    @Autowired
    private IGeonameService geonameService;

    @Autowired
    private IGeonameThirdGeoService geonameThirdGeoService;


    @Override
    public Object getWeatherInfo(int id, double latitude, double longitude, String lang, String unit) {
        return getHefengWeatherInfo(id, latitude, longitude, lang, unit);
    }

    private Map<String, Object> getHefengWeatherInfo(int id, double latitude, double longitude, String lang, String unit) {
        Map<String, Object> weatherInfo = new HashMap<>();
        weatherInfo.put("timestamp", Instant.now().getEpochSecond());
        // 打印日志
        logger.info("id:{}, latitude:{}, longitude:{}, lang:{}, unit:{}", id, latitude, longitude, lang, unit);


        // lang 为空和null时，默认为中文
        String langFormatted = StrUtil.isEmpty(lang) ? "zh" : lang;
        // unit 为空和null时，默认为公制
        String unitFormatted = StrUtil.isEmpty(unit) ? "m" : unit;
        Object hourlyObj;
        Object sevenObj;
        Object nowObj;


        // 构建 Redis 键（key）
        String redisKey = buildRedisKey(id, langFormatted, unitFormatted);
        String redisKeyVO = getWeatherRedisKey(VO_PREFIX, redisKey);
        if (ObjectUtil.isNotNull(redisService.getCacheObject(redisKeyVO))) {
            logger.info("从缓存中获取天气信息VO");
            WeatherInfoVO weatherInfoVO = redisService.getCacheObject(redisKeyVO);
            // 获取hourlyForecast的第一个对象，判断是不是当前小时的00分钟，如果不属于当前小时的00分钟，则走下面的逻辑，不返回，time的格式是10：00
            if (ObjectUtil.isNotNull(weatherInfoVO.getHourlyForecast()) && weatherInfoVO.getHourlyForecast().size() > 0) {
                List<WeatherInfoVO.HourlyVO> hourlyForecast = weatherInfoVO.getHourlyForecast();
                WeatherInfoVO.HourlyVO firstHourlyVO = hourlyForecast.get(0);
                String time = firstHourlyVO.getTime();
                if (StrUtil.isNotEmpty(time) && time.contains(":") && time.endsWith("00")) {
                    String[] split = time.split(":");
                    String hour = split[0];
                    // 获取当前时间的小时和分钟
                    String currentHour = DateUtil.format(new Date(), "HH");
                    // 如果当前时间的小时和分钟和hour和minute相等，则返回
                    if (currentHour.equals(hour)) {
                        weatherInfo.put("weatherInfo", redisService.getCacheObject(redisKeyVO));
                        return weatherInfo;
                    }
                }
            }
        }


        String nowRedisKey = getWeatherRedisKey(NOW_WEATHER_KEY_PREFIX, redisKey);
        String hourlyRedisKey = getWeatherRedisKey(HOURLY_WEATHER_FORECAST_KEY_PREFIX, redisKey);
        String sevenRedisKey = getWeatherRedisKey(SEVEN_WEATHER_FORECAST_KEY_PREFIX, redisKey);
        nowObj = getObject(id, latitude, longitude, langFormatted, unitFormatted, nowRedisKey, NOW_WEATHER_URL);
        hourlyObj = getObject(id, latitude, longitude, langFormatted, unitFormatted, hourlyRedisKey, HOURLY_WEATHER_FORECAST_URL);
        sevenObj = getObject(id, latitude, longitude, langFormatted, unitFormatted, sevenRedisKey, SEVEN_WEATHER_FORECAST_URL);

        WeatherInfoVO weatherInfoVO = new WeatherInfoVO();
        weatherInfoVO.setLang(langFormatted);
        setWeatherInfo(nowObj, weatherInfoVO, hourlyObj, sevenObj);

        //放入缓存
        if (nowObj != null) {
            redisService.setCacheObject(redisKeyVO, weatherInfoVO, 1L, TimeUnit.HOURS);
        }

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
            weatherInfoVO.setVisibility(nowObjJson.getStr("vis"));
            weatherInfoVO.setWind360(nowObjJson.getStr("wind360"));
            weatherInfoVO.setWindDir(nowObjJson.getStr("windDir"));
            weatherInfoVO.setWindScale(nowObjJson.getStr("windScale"));
            weatherInfoVO.setWindSpeed(nowObjJson.getStr("windSpeed"));


            // 以下6个字段无法从nowObj对象中获取到，需从sevenObj对象中获取

            weatherInfoVO.setHourlyForecast(getHourlyForecast(weatherInfoVO, hourlyObj));
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
                // 判断是否小于今天，不包括今天
                if (forecastDate.isBefore(today)) {
                    return;
                }
                // 判断当前天气是否为今天
                if (forecastDate.equals(today)) {
                    weatherInfoVO.setSunrise(itemJson.getStr("sunrise"));
                    weatherInfoVO.setSunset(itemJson.getStr("sunset"));
                    weatherInfoVO.setTemperatureHigh(itemJson.getStr("tempMax"));
                    weatherInfoVO.setTemperatureLow(itemJson.getStr("tempMin"));
                    weatherInfoVO.setUv(itemJson.getStr("uvIndex"));
                }
                //itemJson.getStr("fxDate"))转为时间戳
                itemJson.set("date", DateUtil.parse(fxDate).getTime() / 1000);
                itemJson.set("temperatureLow", itemJson.getStr("tempMin"));
                itemJson.set("temperatureHigh", itemJson.getStr("tempMax"));
                itemJson.set("weather", itemJson.getStr("textDay"));
                itemJson.set("weatherIcon", itemJson.getStr("iconDay"));
                itemJson.set("sunrise", itemJson.getStr("sunrise"));
                itemJson.set("sunset", itemJson.getStr("sunset"));
                itemJson.set("uv", itemJson.getStr("uvIndex"));

                WeatherInfoVO.DailyVO dailyVO = JSONUtil.toBean(itemJson, WeatherInfoVO.DailyVO.class);
                dailyForecast.add(dailyVO);
            });
            return dailyForecast;
        }
        return null;
    }

    private List<WeatherInfoVO.HourlyVO> getHourlyForecast(WeatherInfoVO weatherInfoVO, Object hourlyObj) {
        if (ObjectUtil.isNotEmpty(hourlyObj)) {
            JSONArray hourlyObjJson = JSONUtil.parseArray(hourlyObj);
            List<WeatherInfoVO.HourlyVO> result = new ArrayList<>();

            // 获取当前时间
            OffsetDateTime now = OffsetDateTime.now();
            // 获取当前整点时间
            OffsetDateTime roundedHour = now.withMinute(0).withSecond(0).withNano(0);
            // 格式化当前时间为HH:mm格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = roundedHour.format(formatter);

            // 寻找当前时间的小时对象，并获取其pop值
            String currentHourPop = hourlyObjJson.stream()
                    .map(item -> JSONUtil.parseObj(item))
                    .filter(itemJson -> {
                        String dateTimeString = itemJson.getStr("fxTime");
                        OffsetDateTime dateTime = OffsetDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                        return !dateTime.isBefore(now); // 不早于当前时间，包括当前时间和之后的时间点
                    })
                    .map(itemJson -> itemJson.getStr("pop"))
                    .findFirst()
                    .orElse("0");

            weatherInfoVO.setPop(currentHourPop);

            //把当前时间加上
            WeatherInfoVO.HourlyVO currentHourlyVO = new WeatherInfoVO.HourlyVO();
            currentHourlyVO.setTemperature(weatherInfoVO.getTemperature());
            currentHourlyVO.setWeather(weatherInfoVO.getWeather());
            currentHourlyVO.setWeatherIcon(weatherInfoVO.getWeatherIcon());
            currentHourlyVO.setPop(weatherInfoVO.getPop());
            currentHourlyVO.setTime(formattedTime);
            result.add(currentHourlyVO);

            List<WeatherInfoVO.HourlyVO> hourlyForecast = hourlyObjJson.stream()
                    .map(item -> {
                        JSONObject itemJson = JSONUtil.parseObj(item);
                        itemJson.set("temperature", itemJson.getStr("temp"));
                        itemJson.set("weather", itemJson.getStr("text"));
                        itemJson.set("weatherIcon", itemJson.getStr("icon"));
                        itemJson.set("pop", itemJson.getStr("pop"));
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


    private static String getWeatherRedisKey(String prefix, String redisKey) {
        return WEATHER_PREFIX + getHefengRedisKey(prefix, redisKey);
    }

    private static String getHefengRedisKey(String prefix, String redisKey) {
        return HEFENG_PREFIX + prefix + redisKey;
    }

    private Object getObject(int id, double latitude, double longitude, String langFormatted, String unitFormatted, String redisKey, String weatherForecastUrl) {
        Object weatherObj;
        if (isConditionMet(redisKey)) {
            weatherObj = redisService.getCacheObject(redisKey);
            logger.info("redis缓存中获取数据" + redisKey + ",obj:" + weatherObj);
        } else {
            //纠正经纬度
            GeonameThirdGeo hirdGeo = getGeonameThirdGeo(id);
            if (ObjectUtil.isNotEmpty(hirdGeo)){
                longitude = Double.valueOf(hirdGeo.getLongitude());
                latitude = Double.valueOf(hirdGeo.getLatitude());
            }
            String hourlyWeatherForecast = String.format(weatherForecastUrl, longitude, latitude, langFormatted, unitFormatted);
            String getWeatherForecast = HttpUtil.get(hourlyWeatherForecast);
            //检查getWeatherForecast返回的信息是不是{"code":"404"}
            if ("{\"code\":\"404\"}".equals(getWeatherForecast)) {
                logger.warn("Weather forecast not found: {},result: {}", hourlyWeatherForecast,getWeatherForecast);
                Geoname geoname = getGeoname(id);
                String name = null;
                if (geoname != null) {
                    name = geoname.getName();
                }
                // 请求和风geo接口，获得城市经纬度
                String geoUrl = String.format(HEFENG_GEO_URL, name);
                String geoResult = HttpUtil.get(geoUrl);
                //保存入库
                if (ObjectUtil.isNotEmpty(geoResult)) {
                    JSONObject jsonObject = JSONUtil.parseObj(geoResult);
                    JSONObject jsonObj = (JSONObject) jsonObject.getJSONArray("location").get(0);
                    String latitudeStr = jsonObj.getStr("lat");
                    String longitudeStr = jsonObj.getStr("lon");

                    GeonameThirdGeo geonameThirdGeo = new GeonameThirdGeo();
                    geonameThirdGeo.setGeonameid(id);
                    geonameThirdGeo.setLatitude(latitudeStr);
                    geonameThirdGeo.setLongitude(longitudeStr);
                    geonameThirdGeo.setRequestUrl(geoUrl);
                    geonameThirdGeo.setSourceResponse(geoResult);
                    geonameThirdGeo.setSourceType(0);
                    geonameThirdGeo.setCreateTime(Instant.now().getEpochSecond());
                    geonameThirdGeo.setUpdateTime(Instant.now().getEpochSecond());
                    geonameThirdGeoService.saveOrUpdate(geonameThirdGeo);

                    return getObject(id, Double.parseDouble(latitudeStr), Double.parseDouble(longitudeStr), langFormatted, unitFormatted, redisKey, weatherForecastUrl);
                }

            }
            logger.info("hourlyWeatherForecast:{},getWeatherForecast:{}",hourlyWeatherForecast, getWeatherForecast);

            weatherObj = parseJson(getWeatherForecast);
            if ( weatherObj != null){
                if (redisKey.contains(NOW_WEATHER_KEY_PREFIX)) {
                    redisService.setCacheObject(redisKey, weatherObj, 1L, TimeUnit.HOURS);
                } else if (redisKey.contains(HOURLY_WEATHER_FORECAST_KEY_PREFIX)) {
                    redisService.setCacheObject(redisKey, weatherObj, 12L, TimeUnit.HOURS);
                } else if (redisKey.contains(SEVEN_WEATHER_FORECAST_KEY_PREFIX)) {
                    redisService.setCacheObject(redisKey, weatherObj, 24L, TimeUnit.HOURS);
                }
            }
        }
        return weatherObj;
    }

    private GeonameThirdGeo getGeonameThirdGeo(int id) {
        QueryWrapper<GeonameThirdGeo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("geonameid", id);
        GeonameThirdGeo geonameThirdGeo =  geonameThirdGeoService.getOne(queryWrapper);
        return geonameThirdGeo;
    }

    private Geoname getGeoname(int id) {
        QueryWrapper<Geoname> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("geonameid", id);
        Geoname geoname = geonameService.getOne(queryWrapper);
        return geoname;
    }

    /**
     * 判断redisKey是否有和风天气缓存
     *
     * @param redisKey
     * @return
     */
    public boolean isConditionMet(String redisKey) {
        if (redisService.hasKey(redisKey)) {
            Object weatherObj = redisService.getCacheObject(redisKey);
            if (weatherObj == null) {
                return false;
            }
            if (redisKey.contains(NOW_WEATHER_KEY_PREFIX)) {
                return true;
            } else if (redisKey.contains(HOURLY_WEATHER_FORECAST_KEY_PREFIX)) {
                JSONArray jsonArray = JSONUtil.parseArray(weatherObj);
                OffsetDateTime now = OffsetDateTime.now();
                // 判断数值大于等于这个时间的个数是不是超过12个
                return jsonArray.stream().filter(item -> {
                    JSONObject itemJson = JSONUtil.parseObj(item);
                    String dateTimeString = itemJson.getStr("fxTime");
                    OffsetDateTime dateTime = OffsetDateTime.parse(dateTimeString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    return dateTime.isAfter(now);
                }).count() >= 12;
            } else {
                JSONArray jsonArray = JSONUtil.parseArray(weatherObj);
                // 判断第一个元素是否为当天
                return jsonArray.getJSONObject(0).getStr("fxDate").equals(LocalDate.now().toString());
            }
        }
        return false;
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

    private static String buildRedisKey(int id, String langFormatted, String unitFormatted) {
        // 将 id、latitude、longitude 拼接成字符串作为 Redis 键
        return String.format("%d_%s_%s", id, langFormatted, unitFormatted);
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
