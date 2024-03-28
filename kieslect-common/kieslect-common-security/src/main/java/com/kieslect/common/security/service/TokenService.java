package com.kieslect.common.security.service;


import com.kieslect.common.core.constant.CacheConstants;
import com.kieslect.common.core.constant.SecurityConstants;
import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.utils.JwtUtils;
import com.kieslect.common.core.utils.ServletUtils;
import com.kieslect.common.core.utils.StringUtils;
import com.kieslect.common.redis.service.RedisService;
import com.kieslect.common.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * token验证处理
 * 
 * @author kieslect
 */
@Component
public class TokenService
{
    private static final Logger log = LoggerFactory.getLogger(TokenService.class);

    @Autowired
    private RedisService redisService;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private final static long expireTime = CacheConstants.EXPIRATION;

    private final static String ACCESS_TOKEN = CacheConstants.LOGIN_TOKEN_KEY;

    private final static Long MILLIS_MINUTE_TEN = CacheConstants.REFRESH_TIME * MILLIS_MINUTE;

    /**
     * 创建令牌
     */
    public Map<String, Object> createToken(LoginUserInfo loginUser)
    {
        String userKey = loginUser.getUserKey();
        Long userId = loginUser.getId();
        loginUser.setUserKey(userKey);
        refreshToken(loginUser);

        // Jwt存储信息
        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put(SecurityConstants.USER_KEY, userKey);
        claimsMap.put(SecurityConstants.DETAILS_USER_ID, userId);

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<String, Object>();
        rspMap.put("access_token", JwtUtils.createToken(claimsMap));
        rspMap.put("expires_in", expireTime);
        return rspMap;
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUserInfo getLoginUser()
    {
        return getLoginUser(ServletUtils.getRequest());
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUserInfo getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = SecurityUtils.getToken(request);
        return getLoginUser(token);
    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUserInfo getLoginUser(String token)
    {
        LoginUserInfo user = null;
        try
        {
            if (StringUtils.isNotEmpty(token))
            {
                String userkey = JwtUtils.getUserKey(token);
                user = redisService.getCacheObject(getTokenKey(userkey));
                return user;
            }
        }
        catch (Exception e)
        {
            log.error("获取用户信息异常'{}'", e.getMessage());
        }
        return user;
    }



    /**
     * 删除用户缓存信息
     */
    public void delLoginUser(String token)
    {
        if (StringUtils.isNotEmpty(token))
        {
            String userkey = JwtUtils.getUserKey(token);
            redisService.deleteObject(getTokenKey(userkey));
        }
    }

    public void delLoginUserByUserkey(String userkey)
    {
        if (StringUtils.isNotEmpty(userkey))
        {
            redisService.deleteObject(getTokenKey(userkey));
        }
    }

    /**
     * 验证令牌有效期，相差不足120分钟，自动刷新缓存
     *
     * @param loginUserInfo
     */
    public void verifyToken(LoginUserInfo loginUserInfo)
    {
        long expireTime = loginUserInfo.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUserInfo);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUserInfo 登录信息
     */
    public void refreshToken(LoginUserInfo loginUserInfo)
    {
        loginUserInfo.setLoginTime(System.currentTimeMillis());
        loginUserInfo.setExpireTime(loginUserInfo.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUserInfo.getUserKey());
        redisService.setCacheObject(userKey, loginUserInfo, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String userKey)
    {
        return ACCESS_TOKEN + userKey;
    }
}