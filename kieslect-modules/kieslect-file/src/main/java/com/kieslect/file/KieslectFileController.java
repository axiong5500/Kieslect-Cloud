package com.kieslect.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = {"com.kieslect"})
public class KieslectFileController {
    public static void main(String[] args)
    {
        SpringApplication.run(KieslectFileController.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  文件模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" );
    }
}
