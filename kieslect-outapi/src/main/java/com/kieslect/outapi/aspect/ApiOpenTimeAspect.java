package com.kieslect.outapi.aspect;

import com.kieslect.outapi.annotation.ApiOpenTime;
import com.kieslect.outapi.exception.ApiNotOpenException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
@RefreshScope
public class ApiOpenTimeAspect {

    @Value("${api.open.time.start:18:00:00Z}")
    private String apiOpenTimeStart;

    @Value("${api.open.time.end:23:59:00Z}")
    private String apiOpenTimeEnd;

    @Before("@annotation(apiOpenTime)")
    public void checkApiOpenTime(ApiOpenTime apiOpenTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_TIME;
        LocalTime openTime = LocalTime.parse(apiOpenTimeStart, formatter);
        LocalTime closeTime = LocalTime.parse(apiOpenTimeEnd, formatter);
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneOffset.UTC);
        System.out.println("当前UTC时间：" + currentTime);
        // 获取当前时间的本地时间部分
        LocalTime currentTimeLocal = currentTime.toLocalTime();
        if (currentTimeLocal.isBefore(openTime) || currentTimeLocal.isAfter(closeTime)) {
            String errorMessage = String.format("接口未在开放时间段 %s~%s ，当前UTC时间 %s", apiOpenTimeStart, apiOpenTimeEnd,currentTime);
            throw new ApiNotOpenException(errorMessage);
        }
    }
}
