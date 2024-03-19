package com.kieslect.generator;


import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class MyBatisPlusGenerator {
    public static void main(String[] args) {
        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig
                .Builder("jdbc:mysql://localhost:3306/kieslect_cloud?serverTimezone=Asia/Shanghai",
                "root",
                "123456")
                .build();

        // 全局配置
        GlobalConfig gc = new GlobalConfig.Builder()
                .outputDir(System.getProperty("user.dir")+"/kieslect-generator" + "/src/main/java")
                .author("kieslect")
                .disableOpenDir()
                .build();

        // 包配置
        PackageConfig pc = new PackageConfig.Builder()
                .parent("com.kieslect.user")
                .entity("domain")
                .mapper("mapper")
                .service("service")
                .serviceImpl("service.impl")
                .controller("controller")
                .build();

        // 策略配置
        StrategyConfig strategyConfig = new StrategyConfig.Builder().addInclude("t_user_info").addTablePrefix("t_").entityBuilder()
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .enableChainModel()
                .enableLombok()
                .enableTableFieldAnnotation()
                .build();

        // 生成器
        AutoGenerator ag = new AutoGenerator(dsc);

        // 设置全局配置
        ag.global(gc);
        // 设置包配置
        ag.packageInfo(pc);
        // 设置策略配置
        ag.strategy(strategyConfig);
        // 执行生成
        ag.execute();
    }
}
