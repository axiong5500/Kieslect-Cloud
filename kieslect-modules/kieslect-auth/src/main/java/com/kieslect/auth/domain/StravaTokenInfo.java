package com.kieslect.auth.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * Strava运动员及Token信息表
 * </p>
 *
 * @author kieslect
 * @since 2024-11-11
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_strava_token_info")
public class StravaTokenInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 主键
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * Token 类型，例如: Bearer
     */
      @TableField("token_type")
    private String tokenType;

      /**
     * Token 过期时间戳
     */
      @TableField("expires_at")
    private Long expiresAt;

      /**
     * Token 有效时长（单位：秒）
     */
      @TableField("expires_in")
    private Integer expiresIn;

      /**
     * 刷新 token 字符串
     */
      @TableField("refresh_token")
    private String refreshToken;

      /**
     * 访问 token 字符串
     */
      @TableField("access_token")
    private String accessToken;

      /**
     * 运动员 ID（Strava 用户唯一 ID）
     */
      @TableField("athlete_id")
    private Long athleteId;

      /**
     * 资源状态
     */
      @TableField("resource_state")
    private Integer resourceState;

      /**
     * 运动员名字
     */
      @TableField("firstname")
    private String firstname;

      /**
     * 运动员姓氏
     */
      @TableField("lastname")
    private String lastname;

      /**
     * 城市
     */
      @TableField("city")
    private String city;

      /**
     * 州
     */
      @TableField("state")
    private String state;

      /**
     * 国家
     */
      @TableField("country")
    private String country;

      /**
     * 性别
     */
      @TableField("sex")
    private String sex;

      /**
     * 是否为 Strava 高级会员
     */
      @TableField("premium")
    private Byte premium;

      /**
     * 是否为 Strava 顶级会员
     */
      @TableField("summit")
    private Byte summit;

      /**
     * 徽章类型 ID
     */
      @TableField("badge_type_id")
    private Integer badgeTypeId;

      /**
     * 头像中等大小图片 URL
     */
      @TableField("profile_medium")
    private String profileMedium;

      /**
     * 个人资料 URL
     */
      @TableField("profile")
    private String profile;

      /**
     * 第三方创建时间，用时间戳保存，秒为单位
     */
      @TableField("third_created_at")
    private Long thirdCreatedAt;

      /**
     * 第三方更新时间，用时间戳保存，秒为单位
     */
      @TableField("third_updated_at")
    private Long thirdUpdatedAt;

      /**
     * 创建时间，用时间戳保存，秒为单位
     */
      @TableField("create_time")
    private Long createTime;

      /**
     * 更新时间，用时间戳保存，秒为单位
     */
      @TableField("update_time")
    private Long updateTime;
}
