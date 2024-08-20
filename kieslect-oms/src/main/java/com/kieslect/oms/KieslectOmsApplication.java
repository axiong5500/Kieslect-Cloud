package com.kieslect.oms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.kieslect.**.mapper")
@ComponentScan(basePackages = {"com.kieslect"})
@EnableDiscoveryClient
@RefreshScope
public class KieslectOmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(KieslectOmsApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  OMS服务提供启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
