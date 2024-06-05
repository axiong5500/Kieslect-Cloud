package com.kieslect.weather;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"com.kieslect"})
@MapperScan("com.kieslect.**.mapper")
@EnableDiscoveryClient
public class KieslectWeatherApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(KieslectWeatherApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  天气模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" );
    }
}
