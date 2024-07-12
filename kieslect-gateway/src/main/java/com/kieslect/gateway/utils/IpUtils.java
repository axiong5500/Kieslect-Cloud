package com.kieslect.gateway.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;

public class IpUtils {
    public static String getIpAddr(ServerHttpRequest request) {
        if (request == null) {
            return "unknown";
        }

        String ip = getHeaderValue(request, "x-forwarded-for");
        if (isEmptyOrUnknown(ip)) {
            ip = getHeaderValue(request, "X-Forwarded-For");
        }
        if (isEmptyOrUnknown(ip)) {
            ip = getHeaderValue(request, "X-Real-IP");
        }
        if (isEmptyOrUnknown(ip)) {
            ip = getHeaderValue(request, "Proxy-Client-IP");
        }
        if (isEmptyOrUnknown(ip)) {
            ip = getHeaderValue(request, "WL-Proxy-Client-IP");
        }
        if (isEmptyOrUnknown(ip)) {
            ip = request.getRemoteAddress() != null ? request.getRemoteAddress().getAddress().getHostAddress() : "unknown";
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : getMultistageReverseProxyIp(ip);
    }

    private static String getHeaderValue(ServerHttpRequest request, String headerName) {
        return request.getHeaders().getFirst(headerName);
    }

    private static boolean isEmptyOrUnknown(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    private static String getMultistageReverseProxyIp(String ip) {
        // 处理多级反向代理的IP
        if (ip != null && ip.contains(",")) {
            String[] ips = ip.split(",");
            for (String subIp : ips) {
                if (!isEmptyOrUnknown(subIp)) {
                    return subIp.trim();
                }
            }
        }
        return ip;
    }
}
