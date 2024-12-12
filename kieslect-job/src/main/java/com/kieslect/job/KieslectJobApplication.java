package com.kieslect.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author kieslect
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@ComponentScan(basePackages = {"com.kieslect"})
public class KieslectJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(KieslectJobApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  任务调度启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }

}