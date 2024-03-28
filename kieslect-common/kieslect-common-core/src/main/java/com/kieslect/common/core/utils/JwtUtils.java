package com.kieslect.common.core.utils;


import cn.hutool.jwt.JWTUtil;
import com.kieslect.common.core.constant.SecurityConstants;
import com.kieslect.common.core.constant.TokenConstants;

import java.util.Map;

/**
 * Jwt工具类
 *
 * @author kieslect
 */
public class JwtUtils
{
    public static String secret = TokenConstants.SECRET;

    /**
     * 从数据声明生成令牌
     *
     * @param payload 数据声明
     * @return 令牌
     */
    public static String createToken(Map<String, Object> payload)
    {
        return JWTUtil.createToken(payload, secret.getBytes());
    }



    /**
     * 根据令牌获取用户标识
     * 
     * @param token 令牌
     * @return 用户ID
     */
    public static String getUserKey(String token)
    {
        return JWTUtil.parseToken(token).getPayload().getClaim(SecurityConstants.USER_KEY).toString();
    }


    /**
     * 根据令牌获取用户ID
     * 
     * @param token 令牌
     * @return 用户ID
     */
    public static Long getUserId(String token)
    {
        return Long.valueOf(JWTUtil.parseToken(token)
                .getPayload().getClaim(SecurityConstants.DETAILS_USER_ID).toString());
    }

}
