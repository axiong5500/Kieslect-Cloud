package com.kieslect.device;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@RefreshScope
@MapperScan("com.kieslect.**.mapper")
@ComponentScan(basePackages = {"com.kieslect"})
public class KieslectDeviceController {
    public static void main(String[] args)
    {
        SpringApplication.run(KieslectDeviceController.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  设备模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" );
    }
}
