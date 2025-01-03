package com.kieslect.weather.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements IWeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

    // 免费API Key（每日限额16,700次请求）
    private static final String FREE_API_KEY = "ba0fb55d9b5c49ab8b352808ead4dac5";
    private static final String FREE_API_HOST = "devapi.qweather.com";

    // 收费API Key（无每日请求限制）
    private static final String PAID_API_KEY = "36a411df3f974b53a42ec9c84e2c1139";
    private static final String PAID_API_HOST = "api.qweather.com";

    // 最大重试次数
    private static final int MAX_RETRIES = 2;

    // 使用 AtomicReference 确保线程安全
    private final AtomicReference<ApiConfig> apiConfig = new AtomicReference<>(ApiConfig.freeMode(FREE_API_KEY, FREE_API_HOST));

    /**
     * 用于存储 API 配置的不可变数据类。
     *
     * @param currentApiKey 当前的 API Key
     * @param currentHost   当前的 API Host
     * @param isPremiumMode 是否处于收费模式
     */
    private record ApiConfig(String currentApiKey, String currentHost, boolean isPremiumMode) {
        /**
         * 创建一个免费模式的 ApiConfig 实例。
         */
        public static ApiConfig freeMode(String currentApiKey, String currentHost) {
            return new ApiConfig(currentApiKey, currentHost, false);
        }

        /**
         * 创建一个收费模式的 ApiConfig 实例。
         */
        public static ApiConfig premiumMode(String currentApiKey, String currentHost) {
            return new ApiConfig(currentApiKey, currentHost, true);
        }
    }

    // 切换到收费模式的方法
    private void switchToPremiumMode() {
        apiConfig.set(ApiConfig.premiumMode(PAID_API_KEY, PAID_API_HOST));
    }

    // 切换到免费模式的方法
    private void switchToFreeMode() {
        apiConfig.set(ApiConfig.freeMode(FREE_API_KEY, FREE_API_HOST));
    }

    // 每小时尝试切换回免费模式
    @Async
    @Scheduled(cron = "0 0 0/1 * * ?")
    public void trySwitchBackToFreeMode() {
        logger.info("尝试切换回免费模式...");
        ApiConfig config = apiConfig.get();
        if (config.isPremiumMode) {
            try {
                String testUrl = String.format(NOW_WEATHER_URL, FREE_API_HOST, FREE_API_KEY, "113.0", "23.0");
                String result = HttpUtil.get(testUrl);
                logger.info("免费模式测试结果：" + result);
                JSONObject jsonObject = JSONUtil.parseObj(result);
                String code = jsonObject.getStr("code");
                if ("200".equals(code)) {
                    switchToFreeMode();
                    logger.info("已切换回免费模式");
                } else {
                    logger.info("免费模式仍限流，保持收费模式");
                }
            } catch (Exception ex) {
                logger.error("切换回免费模式失败", ex);
            }
        }
    }

    // 实况天气 经度（longitude）在前 纬度（latitude）在后
    private static final String NOW_WEATHER_URL = "https://%s/v7/weather/now?key=%s&location=%s,%s";
    // 逐小时预报（未来24小时） 经度（longitude）在前 纬度（latitude）在后
    private static final String HOURLY_WEATHER_FORECAST_URL = "https://%s/v7/weather/24h?key=%s&location=%s,%s";

    // 7天预报 经度（longitude）在前 纬度（latitude）在后
    private static final String SEVEN_WEATHER_FORECAST_URL = "https://%s/v7/weather/7d?key=%s&location=%s,%s";

    // 和风geo
    private static final String HEFENG_GEO_URL = "https://geoapi.qweather.com/v2/city/lookup?key=%s&location=%s&number=1";


    private static final String WEATHER_PREFIX = "weather:";

    private static final String HEFENG_PREFIX = "hefeng:";
    private static final String ZONEID_PREFIX = "zoneid:";

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
        String redisKey = buildRedisKey(id);
        String redisKeyVO = getWeatherRedisKey(VO_PREFIX, redisKey);
        Map<String, Object> cacheWeatherInfo = getCacheWeatherInfo(id, redisKeyVO, weatherInfo);
        if (cacheWeatherInfo != null) {
            return cacheWeatherInfo;
        }


        String nowRedisKey = getWeatherRedisKey(NOW_WEATHER_KEY_PREFIX, redisKey);
        String hourlyRedisKey = getWeatherRedisKey(HOURLY_WEATHER_FORECAST_KEY_PREFIX, redisKey);
        String sevenRedisKey = getWeatherRedisKey(SEVEN_WEATHER_FORECAST_KEY_PREFIX, redisKey);
        nowObj = getObject(id, latitude, longitude, langFormatted, unitFormatted, nowRedisKey, NOW_WEATHER_URL,MAX_RETRIES);
        hourlyObj = getObject(id, latitude, longitude, langFormatted, unitFormatted, hourlyRedisKey, HOURLY_WEATHER_FORECAST_URL,MAX_RETRIES);
        sevenObj = getObject(id, latitude, longitude, langFormatted, unitFormatted, sevenRedisKey, SEVEN_WEATHER_FORECAST_URL,MAX_RETRIES);

        WeatherInfoVO weatherInfoVO = new WeatherInfoVO();
        weatherInfoVO.setLang(langFormatted);
        setWeatherInfo(id, nowObj, weatherInfoVO, hourlyObj, sevenObj);

        //放入缓存
        if (nowObj != null) {
            redisService.setCacheObject(redisKeyVO, weatherInfoVO, 1L, TimeUnit.HOURS);
        }

        weatherInfo.put("weatherInfo", weatherInfoVO);

        return weatherInfo;
    }

    private Map<String, Object> getCacheWeatherInfo(int id, String redisKeyVO, Map<String, Object> weatherInfo) {
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
                    // 获取当前时间的小时
                    ZonedDateTime zonedNow = getZonedDateTime(id);
                    String currentHour = DateUtil.format(zonedNow.toLocalDateTime(), "HH");
                    // 如果当前时间的小时和hour相等，则返回
                    if (currentHour.equals(hour)) {
                        weatherInfo.put("weatherInfo", redisService.getCacheObject(redisKeyVO));
                        return weatherInfo;
                    }
                }
            }
        }
        return null;
    }

    private ZonedDateTime getZonedDateTime(int id) {
        // 指定时区ID
        String timeZoneId = getTimezone(id);
        // 获取当前时间在指定时区中的时间
        ZonedDateTime zonedNow = ZonedDateTime.now(ZoneId.of(timeZoneId));
        logger.info("zonedNow:{}", zonedNow);
        return zonedNow;
    }

    private String getTimezone(int id) {
        //从缓存中获取timezoneId
        String zoneIdRedisKey = getWeatherRedisKey(ZONEID_PREFIX, buildRedisKey(id));
        if (ObjectUtil.isNotNull(redisService.getCacheObject(zoneIdRedisKey))) {
            return redisService.getCacheObject(zoneIdRedisKey);
        }
        //根据ID查出timezoneId
        LambdaQueryWrapper<Geoname> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Geoname::getGeonameid, id);
        String timezone = geonameService.getOne(queryWrapper).getTimezone();
        //放入缓存永久时间
        redisService.setCacheObject(zoneIdRedisKey, timezone);
        return timezone;
    }

    private void setWeatherInfo(int id, Object nowObj, WeatherInfoVO weatherInfoVO, Object hourlyObj, Object sevenObj) {
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
            weatherInfoVO.setPressure(nowObjJson.getStr("pressure"));


            // 以下6个字段无法从nowObj对象中获取到，需从sevenObj对象中获取

            weatherInfoVO.setHourlyForecast(getHourlyForecast(id, weatherInfoVO, hourlyObj));
            weatherInfoVO.setDailyForecast(getDailyForecast(id, weatherInfoVO, sevenObj));
        }
    }

    private List<WeatherInfoVO.DailyVO> getDailyForecast(int id, WeatherInfoVO weatherInfoVO, Object sevenObj) {
        if (ObjectUtil.isNotEmpty(sevenObj)) {
            JSONArray sevenObjJson = JSONUtil.parseArray(sevenObj);
            ZonedDateTime zonedNow = getZonedDateTime(id);
            LocalDate today = zonedNow.toLocalDate();
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
                itemJson.set("date", LocalDate.parse(fxDate).atStartOfDay(ZoneOffset.UTC).toInstant().getEpochSecond());
                itemJson.set("temperatureLow", itemJson.getStr("tempMin"));
                itemJson.set("temperatureHigh", itemJson.getStr("tempMax"));
                itemJson.set("weather", itemJson.getStr("textDay"));
                itemJson.set("weatherIcon", itemJson.getStr("iconDay"));
                itemJson.set("sunrise", itemJson.getStr("sunrise"));
                itemJson.set("sunset", itemJson.getStr("sunset"));
                itemJson.set("uv", itemJson.getStr("uvIndex"));
                itemJson.set("pressure", itemJson.getStr("pressure"));

                WeatherInfoVO.DailyVO dailyVO = JSONUtil.toBean(itemJson, WeatherInfoVO.DailyVO.class);
                dailyForecast.add(dailyVO);
            });
            return dailyForecast;
        }
        return null;
    }

    private List<WeatherInfoVO.HourlyVO> getHourlyForecast(int id, WeatherInfoVO weatherInfoVO, Object hourlyObj) {
        if (ObjectUtil.isNotEmpty(hourlyObj)) {
            JSONArray hourlyObjJson = JSONUtil.parseArray(hourlyObj);
            List<WeatherInfoVO.HourlyVO> result = new ArrayList<>();

            // 指定时区ID
            ZonedDateTime zonedNow = getZonedDateTime(id);
            // 将分钟和秒数设置为0，获取当前整点时间
            ZonedDateTime roundedHourTime = zonedNow.withMinute(0).withSecond(0).withNano(0);
            // 格式化当前时间为HH:mm格式
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = roundedHourTime.format(formatter);

            // 寻找当前时间的小时对象，并获取其pop值
            String currentHourPop = hourlyObjJson.stream()
                    .map(item -> JSONUtil.parseObj(item))
                    .filter(itemJson -> {
                        String dateTimeString = itemJson.getStr("fxTime");
                        ZonedDateTime dateTime = ZonedDateTime.parse(dateTimeString);
                        return !dateTime.isBefore(zonedNow); // 不早于当前时间，包括当前时间和之后的时间点
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
            currentHourlyVO.setPressure(weatherInfoVO.getPressure());
            result.add(currentHourlyVO);

            List<WeatherInfoVO.HourlyVO> hourlyForecast = hourlyObjJson.stream()
                    .map(item -> {
                        JSONObject itemJson = JSONUtil.parseObj(item);
                        itemJson.set("temperature", itemJson.getStr("temp"));
                        itemJson.set("weather", itemJson.getStr("text"));
                        itemJson.set("weatherIcon", itemJson.getStr("icon"));
                        itemJson.set("pop", itemJson.getStr("pop"));
                        itemJson.set("pressure", itemJson.getStr("pressure"));
                        String dateTimeString = itemJson.getStr("fxTime");
                        ZonedDateTime dateTime = ZonedDateTime.parse(dateTimeString);


                        // 判断时间是否是大于当前时间的下一个小时，如果不是则返回 null
                        if (dateTime.isAfter(zonedNow)) {
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

    private Object getObject(int id, double latitude, double longitude, String langFormatted, String unitFormatted, String redisKey, String weatherForecastUrl, int retries) {
        Object weatherObj;
        // 如果已达到最大重试次数，直接返回空或报错
        if (retries <= 0) {
            logger.error("已达到最大重试次数，无法获取有效天气数据.");
            return null;
        }
        if (isConditionMet(id, redisKey)) {
            weatherObj = redisService.getCacheObject(redisKey);
            logger.info("命中缓存：{}，从缓存中获得数据",redisKey);
        } else {
            //纠正经纬度
            GeonameThirdGeo hirdGeo = getGeonameThirdGeo(id);
            if (ObjectUtil.isNotEmpty(hirdGeo)) {
                longitude = Double.valueOf(hirdGeo.getLongitude());
                latitude = Double.valueOf(hirdGeo.getLatitude());
            }
            String hourlyWeatherForecast = String.format(weatherForecastUrl, apiConfig.get().currentHost(), apiConfig.get().currentApiKey(), longitude, latitude);
            String getWeatherForecast = HttpUtil.get(hourlyWeatherForecast);
            //检查getWeatherForecast返回的信息是不是{"code":"404"},说明是经纬度不是和风的经纬度，需要通过请求和风经纬度接口，获取正确经纬保存入库，再次用正确经纬度请求获取天气数据
            if (StrUtil.isNotEmpty(getWeatherForecast)){
                JSONObject weatherForecastJson = JSONUtil.parseObj(getWeatherForecast);
                String code = weatherForecastJson.getStr("code");
                if (code.equals("429") || code.equals("402")){
                    // 429错误，说明请求次数过多，需要请求收费接口再请求
                    switchToPremiumMode();
                    return getObject(id, latitude, longitude, langFormatted, unitFormatted, redisKey, weatherForecastUrl,retries-1);
                }
                if (code.equals("404")) {
                    logger.warn("Weather forecast not found: {},result: {}", hourlyWeatherForecast, getWeatherForecast);
                    Geoname geoname = getGeoname(id);
                    String name = null;
                    if (geoname != null) {
                        name = geoname.getName();
                    }
                    // 请求和风geo接口，获得城市经纬度
                    String geoUrl = String.format(HEFENG_GEO_URL, apiConfig.get().currentApiKey(), name);
                    String geoResult = HttpUtil.get(geoUrl);
                    JSONObject geoResultJson = JSONUtil.parseObj(geoResult);
                    String geoResultCode = geoResultJson.getStr("code");
                    if (geoResultCode.equals("429") || geoResultCode.equals("402")){
                        // 429错误，说明请求次数过多，需要请求收费接口再请求
                        switchToPremiumMode();
                        return getObject(id, latitude, longitude, langFormatted, unitFormatted, redisKey, weatherForecastUrl,retries-1);
                    }
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

                        return getObject(id, Double.parseDouble(latitudeStr), Double.parseDouble(longitudeStr), langFormatted, unitFormatted, redisKey, weatherForecastUrl,retries -1);
                    }

                }
            }

            logger.info("hourlyWeatherForecast:{},getWeatherForecast:{}", hourlyWeatherForecast, getWeatherForecast);

            weatherObj = parseJson(getWeatherForecast);
            if (weatherObj != null) {
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
        GeonameThirdGeo geonameThirdGeo = geonameThirdGeoService.getOne(queryWrapper);
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
    public boolean isConditionMet(int id, String redisKey) {
        if (redisService.hasKey(redisKey)) {
            Object weatherObj = redisService.getCacheObject(redisKey);
            if (weatherObj == null) {
                return false;
            }
            if (redisKey.contains(NOW_WEATHER_KEY_PREFIX)) {
                return true;
            } else if (redisKey.contains(HOURLY_WEATHER_FORECAST_KEY_PREFIX)) {
                JSONArray jsonArray = JSONUtil.parseArray(weatherObj);
                // 指定时区ID
                ZonedDateTime zonedNow = getZonedDateTime(id);
                // 判断数值大于等于这个时间的个数是不是超过12个
                return jsonArray.stream().filter(item -> {
                    JSONObject itemJson = JSONUtil.parseObj(item);
                    String dateTimeString = itemJson.getStr("fxTime");
                    ZonedDateTime dateTime = ZonedDateTime.parse(dateTimeString);
                    return dateTime.isAfter(zonedNow);
                }).count() >= 12;
            } else {
                JSONArray jsonArray = JSONUtil.parseArray(weatherObj);
                // 指定时区ID
                ZonedDateTime zonedNow = getZonedDateTime(id);
                // 判断第一个元素是否为当天
                String firstDate = jsonArray.getJSONObject(0).getStr("fxDate");
                // 解析给定的日期字符串为 LocalDate 对象
                LocalDate firstLocalDate = LocalDate.parse(firstDate);
                return firstLocalDate.equals(zonedNow.toLocalDate());
            }
        }
        return false;
    }

    private static String buildRedisKey(int id) {
        // 将 id 作为 Redis 键
        return String.format("%d", id);
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


    public static void main(String[] args) {
        // 指定时区ID
        String timeZoneId = "America/New_York"; // 可以替换为任何有效的时区ID

        // 获取当前时间在指定时区中的时间
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of(timeZoneId));

        // 格式化输出
        String formattedDateTime = zonedDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z"));

        // 输出指定时区的当前时间
        System.out.println("当前时间在 " + timeZoneId + " 时区中的时间为: " + formattedDateTime);

        // 获取当前整点时间
        // 将分钟和秒数设置为0，获取当前整点时间
        ZonedDateTime roundedHourTime = zonedDateTime.withMinute(0).withSecond(0).withNano(0);

        // 格式化当前时间为HH:mm格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String formattedTime = roundedHourTime.format(formatter);

        System.out.println("格式化当前时间为HH:mm格式: " + formattedTime);
    }


}
