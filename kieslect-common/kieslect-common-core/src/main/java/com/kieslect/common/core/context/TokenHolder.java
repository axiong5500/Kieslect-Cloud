package com.kieslect.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;

public class TokenHolder {
    private static final TransmittableThreadLocal<String> tokenThreadLocal = new TransmittableThreadLocal<>();

    public static void setToken(String token) {
        tokenThreadLocal.set(token);
    }

    public static String getToken() {
        return tokenThreadLocal.get();
    }

    public static void clear() {
        tokenThreadLocal.remove();
    }
}
