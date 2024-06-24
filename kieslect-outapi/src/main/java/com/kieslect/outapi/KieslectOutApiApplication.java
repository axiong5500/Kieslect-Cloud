package com.kieslect.outapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.kieslect.**.mapper")
@ComponentScan(basePackages = {"com.kieslect"})
@EnableDiscoveryClient
@RefreshScope
@EnableAspectJAutoProxy
public class KieslectOutApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(KieslectOutApiApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  第三方api服务提供启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
