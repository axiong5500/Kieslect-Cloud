package com.kieslect.device.utils;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.CountryResponse;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.InetAddress;

public class GeoIPUtil {
    private static final String DB_PATH = "GeoLite2-City.mmdb";
    private static DatabaseReader dbReader;

    static {
        try {
            // 初始化数据库读取器
            ClassPathResource resource = new ClassPathResource(DB_PATH);
            dbReader = new DatabaseReader.Builder(resource.getInputStream()).build();
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize GeoIP database reader", e);
        }
    }

    /**
     * 根据IP地址获取国家代码
     *
     * @param ip IP地址
     * @return 国家ISO代码
     */
    public static String getCountryCode(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse response = dbReader.country(ipAddress);
            return response.getCountry().getIsoCode();  // 返回国家的ISO代码
        } catch (IOException | GeoIp2Exception e) {
            return null; // 在生产环境中，建议处理错误日志或抛出自定义异常
        }
    }

    /**
     * 根据IP地址获取城市名称
     *
     * @param ip IP地址
     * @return 城市名称
     */
    public static String getCity(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);
            return response.getCity().getName();  // 返回城市名称
        } catch (IOException | GeoIp2Exception e) {
            return null;
        }
    }

    /**
     * 根据IP地址获取省份名称
     *
     * @param ip IP地址
     * @return 省份名称
     */
    public static String getProvince(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CityResponse response = dbReader.city(ipAddress);
            return response.getMostSpecificSubdivision().getName();  // 返回省份名称
        } catch (IOException | GeoIp2Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        String ip = "58.8.13.184";
        String countryCode = getCountryCode(ip);
        String city = getCity(ip);
        String province = getProvince(ip);
        System.out.println("Country Code: " + countryCode);
        System.out.println("Province: " + province);
        System.out.println("City: " + city);
    }
}
