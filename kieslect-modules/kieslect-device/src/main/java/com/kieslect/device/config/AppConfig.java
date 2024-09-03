package com.kieslect.device.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class AppConfig {
    /**
     * 配置自定义线程池用于处理异步任务
     *
     * @return 配置好的线程池
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 设置核心线程数
        executor.setCorePoolSize(10);

        // 设置最大线程数
        executor.setMaxPoolSize(20);

        // 设置队列容量
        executor.setQueueCapacity(50);

        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);

        // 设置线程名前缀
        executor.setThreadNamePrefix("Async-");

        // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 初始化线程池
        executor.initialize();

        return executor;
    }
}
