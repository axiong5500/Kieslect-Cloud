package com.kieslect.gateway.config;

import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.enums.ResponseCodeEnum;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.common.security.utils.SecurityUtils;
import com.kieslect.gateway.utils.IpUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Configuration
public class GatewayConfig {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(GatewayConfig.class);

    @Autowired
    private TokenService tokenService;

    // 定义白名单，即不需要 Token 验证的路径
    private static final String[] WHITELIST = {
            "/public/**",
            "/kieslect-auth/auth/sendCaptcha",
            "/kieslect-auth/auth/register",
            "/kieslect-auth/auth/forgetPassword",
            "/kieslect-auth/auth/login",
            "/kieslect-auth/auth/third/login",
            "/kieslect-auth/auth/logout",
            "/kieslect-user/user/notify/getAppList",
            "/kieslect-device/device/**/sys/**",
            "/kieslect-device/device/getList",
            "/kieslect-device/device/ota/getList",
            "/kieslect-device/device/dial/getList",
            "/kieslect-device/device/appManage/getApp",
            "/kieslect-file/file/upload",
            "/kieslect-file/file/v2/**",
            "/kieslect-file/file/download/**",
            "/kieslect-weather/weather/getWeatherInfo",
            "/kieslect-**/**/task/**",
            "/kieslect-outapi/**",
    };

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 所有需要 Token 验证的路径
                .route("auth_service", r -> r.path("/kieslect-auth/**")
                        .filters(f -> f.filter(tokenValidationFilter()).rewritePath("/kieslect-auth/(?<path>.*)", "/${path}"))
                        .uri("lb://kieslect-auth"))
                .route("file_service", r -> r.path("/kieslect-file/**")
                        .filters(f -> f.filter(tokenValidationFilter()).rewritePath("/kieslect-file/(?<path>.*)", "/${path}"))
                        .uri("lb://kieslect-file"))
                .route("user_service", r -> r.path("/kieslect-user/**")
                        .filters(f -> f.filter(tokenValidationFilter()).rewritePath("/kieslect-user/(?<path>.*)", "/${path}"))
                        .uri("lb://kieslect-user"))
                .route("device_service", r -> r.path("/kieslect-device/**")
                        .filters(f -> f.filter(tokenValidationFilter()).rewritePath("/kieslect-device/(?<path>.*)", "/${path}"))
                        .uri("lb://kieslect-device"))
                .route("weather_service", r -> r.path("/kieslect-weather/**")
                        .filters(f -> f.filter(tokenValidationFilter()).rewritePath("/kieslect-weather/(?<path>.*)", "/${path}"))
                        .uri("lb://kieslect-weather"))
                .route("outapi_service", r -> r.path("/kieslect-outapi/**")
                        .filters(f -> f.filter(tokenValidationFilter()).rewritePath("/kieslect-outapi/(?<path>.*)", "/${path}"))
                        .uri("lb://kieslect-outapi"))
                .build();
    }

    @Bean
    public GatewayFilter tokenValidationFilter() {
        return (exchange, chain) -> {
            // 获取客户端请求对象
            ServerHttpRequest request = exchange.getRequest();
            String clientIp = IpUtils.getIpAddr(request);

            // 获取请求路径
            String requestPath = request.getPath().value();


            // 如果请求路径在白名单中，则直接放行
            if (isInWhiteList(requestPath)) {
                // 将请求上下文信息添加到 HTTP 头部
                ServerHttpRequest modifiedRequest = request.mutate()
                        .header("X-Client-IP", clientIp)
                        .build();
                //输出日志
                logger.info("客户端IP：{}，检测到白名单请求路径：{}", clientIp, requestPath);
                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }

            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            // 在实际情况中，你需要根据你的认证机制和 Token 格式来解析 Token，并检查其是否过期
            // 在这里你可以调用你的认证服务/中间件来验证 Token 是否有效

            // 假设这里直接判断 Token 是否存在，并简单假设所有 Token 都是有效的
            if (token == null || token.isEmpty()) {
                // 如果 Token 不存在或为空，返回 401 Unauthorized 错误
                logger.info("客户端IP：{}，请求路径：{}，无效的token：{}", clientIp, requestPath, token);
                return onError(exchange, ResponseCodeEnum.UNAUTHORIZED, null);
            }


            String replaceTokenPrefix = SecurityUtils.replaceTokenPrefix(token);
            LoginUserInfo loginUser = tokenService.getLoginUser(replaceTokenPrefix);
            // loginUser 不为空，说明 Token 有效
            if (loginUser == null) {
                // 如果 Token 存在且有效，继续处理请求
                logger.info("客户端IP：{}，请求路径：{}，缓存上不存在该token：{}", clientIp, requestPath, token);
                return onError(exchange, ResponseCodeEnum.UNAUTHORIZED, null);
            }
            tokenService.verifyToken(loginUser);

            // 将请求上下文信息添加到 HTTP 头部
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("X-Client-IP", clientIp)
                    .header("X-User-ID", String.valueOf(loginUser.getKid())) // 根据需要设置其他头部
                    .build();
            // 如果 Token 存在且有效，继续处理请求
            logger.info("客户端IP：{}，请求路径：{}，有效token：{}", clientIp, requestPath, token);
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    // 判断请求路径是否在白名单中的方法
    private boolean isInWhiteList(String requestPath) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for (String path : WHITELIST) {
            if (antPathMatcher.match(path, requestPath)) {
                return true;
            }
        }
        return false;
    }


    private Mono<Void> onError(ServerWebExchange exchange, ResponseCodeEnum responseCode, String data) {
        ServerHttpResponse response = exchange.getResponse();
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
        // 构建响应体
        String errorResponse = "{\"code\": " + responseCode.getCode() + ", \"msg\": \"" + responseCode.getMessage() + "\", \"data\": " + data + "}";
        byte[] responseBytes = errorResponse.getBytes();
        // 设置响应头
        exchange.getResponse().getHeaders().setContentLength(responseBytes.length);

        // 写入响应体
        DataBuffer buffer = response.bufferFactory().wrap(responseBytes);
        return response.writeWith(Flux.just(buffer));
    }
}
