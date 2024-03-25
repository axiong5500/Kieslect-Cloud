package com.kieslect.common.core.context;

import com.kieslect.common.core.domain.LoginUserInfo;
import org.springframework.stereotype.Component;

@Component
public class RequestContextHolder {
    private static final ThreadLocal<LoginUserInfo> loginUserHolder = new ThreadLocal<>();

    public static LoginUserInfo getLoginUser() {
        return loginUserHolder.get();
    }

    public static void setLoginUser(LoginUserInfo loginUser) {
        loginUserHolder.set(loginUser);
    }

    public static void clearLoginUser() {
        loginUserHolder.remove();
    }

}
