package com.kieslect.auth.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.auth.domain.StravaTokenInfo;
import com.kieslect.auth.domain.vo.StravaRefreshTokenVO;
import com.kieslect.auth.mapper.StravaTokenInfoMapper;
import com.kieslect.auth.service.IStravaTokenInfoService;
import com.kieslect.common.redis.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Strava运动员及Token信息表 服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-11-11
 */
@Service
public class StravaTokenInfoServiceImpl extends ServiceImpl<StravaTokenInfoMapper, StravaTokenInfo> implements IStravaTokenInfoService {

    private static final Logger logger = LoggerFactory.getLogger(StravaTokenInfoServiceImpl.class);

    @Autowired
    private StravaTokenInfoMapper stravaTokenInfoMapper;
    @Autowired
    RedisService redisService;
    private static final String STRAVA_TOKEN = "strava_token:";
    //Strava域名
    private static final String STRAVA_DOMAIN = "https://www.strava.com";
    // 获取Token url
    private static final String REFRESH_TOKEN_URL = STRAVA_DOMAIN + "/api/v3/oauth/token";
    // 删除Token url
    private static final String DELETE_TOKEN_URL = "/oauth/deauthorize";

    private static final String CLIENT_ID = "129194";
    private static final String CLIENT_SECRET = "fd18b16ec45236ebd80db866aa2961688a2df842";
    private static final String GRANT_TYPE = "refresh_token";

    public static String getStravaTokenRedisKey(String redisKey) {
        return STRAVA_TOKEN + redisKey;
    }


    @Override
    public void insertOrUpdateStravaTokenInfo(StravaTokenInfo stravaTokenInfo) {
        stravaTokenInfoMapper.insertOrUpdateStravaTokenInfo(stravaTokenInfo);
        // 缓存起来，用于file工程刷新Strava授权
        redisService.setCacheObject(getStravaTokenRedisKey(String.valueOf(stravaTokenInfo.getAthleteId())), stravaTokenInfo);
    }

    @Override
    public String getRefreshToken(String refreshToken) {
        try {
            // 1. 获取新的 Token 数据
            StravaRefreshTokenVO stravaRefreshTokenVO = fetchAndParseRefreshToken(refreshToken);
            if (stravaRefreshTokenVO == null) {
                logger.error("Failed to fetch or parse Strava refresh token.");
                return null;
            }

            // 2. 更新数据库
            updateTokenInDatabase(refreshToken, stravaRefreshTokenVO);

            // 3. 从数据库获取最新 Token 信息
            StravaTokenInfo stravaTokenInfo = getTokenFromDatabase(refreshToken);
            if (stravaTokenInfo == null) {
                logger.error("Failed to retrieve StravaTokenInfo from database for refreshToken: " + refreshToken);
                return null;
            }

            // 4. 缓存到 Redis
            cacheTokenInRedis(stravaTokenInfo);

            return stravaTokenInfo.getAccessToken();
        } catch (Exception e) {
            logger.error("Error refreshing Strava token: ", e);
            return null;
        }
    }

    private StravaRefreshTokenVO fetchAndParseRefreshToken(String refreshToken) {
        try {
            String result = fetchRefreshToken(refreshToken);
            return JSONUtil.toBean(result, StravaRefreshTokenVO.class);
        } catch (Exception e) {
            logger.error("Error fetching or parsing refresh token: ", e);
            return null;
        }
    }

    private void updateTokenInDatabase(String refreshToken, StravaRefreshTokenVO tokenVO) {
        Long expiresAt = tokenVO.getExpiresAt();
        Integer expiresIn = tokenVO.getExpiresIn();
        String accessToken = tokenVO.getAccessToken();

        UpdateWrapper<StravaTokenInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("refresh_token", refreshToken);
        StravaTokenInfo updateEntity = new StravaTokenInfo()
                .setAccessToken(accessToken)
                .setExpiresAt(expiresAt)
                .setExpiresIn(expiresIn);

        int rowsUpdated = stravaTokenInfoMapper.update(updateEntity, updateWrapper);
        if (rowsUpdated == 0) {
            logger.warn("No rows updated for refreshToken: " + refreshToken);
        }
    }

    private StravaTokenInfo getTokenFromDatabase(String refreshToken) {
        return stravaTokenInfoMapper.selectOne(
                new LambdaQueryWrapper<StravaTokenInfo>().eq(StravaTokenInfo::getRefreshToken, refreshToken)
        );
    }

    private void cacheTokenInRedis(StravaTokenInfo tokenInfo) {

        String redisKey = getStravaTokenRedisKey(String.valueOf(tokenInfo.getAthleteId()));

        redisService.setCacheObject(redisKey, tokenInfo);
        logger.info("Cached Strava token for athleteId: " + tokenInfo.getAthleteId());
    }


    private String fetchRefreshToken(String refreshToken) {

        // 使用 Hutool 发起 POST 请求，向 Strava 发送请求获取 Access Token
        HttpResponse response = HttpRequest.post(REFRESH_TOKEN_URL)
                .form("client_id", CLIENT_ID)
                .form("client_secret", CLIENT_SECRET)
                .form("refresh_token", refreshToken)
                .form("grant_type", GRANT_TYPE)
                .execute();

        // 返回响应的 body 内容（Access Token 或错误信息）
        return response.body();
    }
}
