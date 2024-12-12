package com.kieslect.auth.service;

import com.kieslect.auth.domain.StravaTokenInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * Strava运动员及Token信息表 服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-11-11
 */
public interface IStravaTokenInfoService extends IService<StravaTokenInfo> {
    // 插入或更新StravaTokenInfo
    void insertOrUpdateStravaTokenInfo(StravaTokenInfo stravaTokenInfo);

    String getRefreshToken(String refreshToken);
}
