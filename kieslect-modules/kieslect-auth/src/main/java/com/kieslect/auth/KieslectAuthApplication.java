package com.kieslect.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.kieslect.**.mapper")
@ComponentScan(basePackages = {"com.kieslect"})
@EnableDiscoveryClient
public class KieslectAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(KieslectAuthApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  认证授权中心启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
