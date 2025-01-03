package com.kieslect.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.kieslect.**.mapper")
@ComponentScan(basePackages = {"com.kieslect"})
@EnableDiscoveryClient
public class KieslectUserApplication {
    public static void main(String[] args)
    {
        SpringApplication.run(KieslectUserApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  用户模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" );
    }
}
