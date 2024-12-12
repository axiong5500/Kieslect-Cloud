package com.kieslect.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kieslect.auth.domain.StravaTokenInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Strava运动员及Token信息表 Mapper 接口
 * </p>
 *
 * @author kieslect
 * @since 2024-11-11
 */
public interface StravaTokenInfoMapper extends BaseMapper<StravaTokenInfo> {
    @Insert("<script>" +
            "INSERT INTO t_strava_token_info (" +
            "token_type, expires_at, expires_in, refresh_token, access_token, athlete_id, resource_state, " +
            "firstname, lastname, city, state, country, sex, premium, summit, badge_type_id, profile_medium, " +
            "profile, third_created_at, third_updated_at, create_time, update_time" +
            ") VALUES (" +
            "#{tokenInfo.tokenType}, #{tokenInfo.expiresAt}, #{tokenInfo.expiresIn}, #{tokenInfo.refreshToken}, " +
            "#{tokenInfo.accessToken}, #{tokenInfo.athleteId}, #{tokenInfo.resourceState}, " +
            "#{tokenInfo.firstname}, #{tokenInfo.lastname}, #{tokenInfo.city}, #{tokenInfo.state}, " +
            "#{tokenInfo.country}, #{tokenInfo.sex}, #{tokenInfo.premium}, #{tokenInfo.summit}, " +
            "#{tokenInfo.badgeTypeId}, #{tokenInfo.profileMedium}, #{tokenInfo.profile}, " +
            "#{tokenInfo.thirdCreatedAt}, #{tokenInfo.thirdUpdatedAt}, #{tokenInfo.createTime}, #{tokenInfo.updateTime}" +
            ") " +
            "ON DUPLICATE KEY UPDATE " +
            "token_type = VALUES(token_type), expires_at = VALUES(expires_at), expires_in = VALUES(expires_in), " +
            "refresh_token = VALUES(refresh_token), access_token = VALUES(access_token), " +
            "resource_state = VALUES(resource_state), firstname = VALUES(firstname), lastname = VALUES(lastname), " +
            "city = VALUES(city), state = VALUES(state), country = VALUES(country), sex = VALUES(sex), " +
            "premium = VALUES(premium), summit = VALUES(summit), badge_type_id = VALUES(badge_type_id), " +
            "profile_medium = VALUES(profile_medium), profile = VALUES(profile), " +
            "third_created_at = VALUES(third_created_at), third_updated_at = VALUES(third_updated_at), " +
            "update_time = VALUES(update_time)" +
            "</script>")
    void insertOrUpdateStravaTokenInfo(@Param("tokenInfo") StravaTokenInfo stravaTokenInfo);
}
