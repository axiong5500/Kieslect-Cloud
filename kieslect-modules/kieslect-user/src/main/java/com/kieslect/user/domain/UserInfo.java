package com.kieslect.user.domain;

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
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-04-18
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_user_info")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 用户key
     */
      @TableField("user_key")
    private String userKey;

      /**
     * 账号
     */
      @TableField("account")
    private String account;

      /**
     * 邮箱
     */
      @TableField("email")
    private String email;

      /**
     * 密码
     */
      @TableField("password")
    private String password;

      /**
     * 第三方授权码
     */
      @TableField("third_token")
    private String thirdToken;

      /**
     * 第三方授权类型，0：google，1：facebook，2：apple，3：wechat
     */
      @TableField("third_token_type")
    private Byte thirdTokenType;

      /**
     * 性别，0：男，女：1，2：其它
     */
      @TableField("sex")
    private Byte sex;

    /**
     * 性别，0：男，女：1，2：其它
     */
    @TableField("new_sex")
    private Byte newSex;

      /**
     * 生日
     */
      @TableField("birthday")
    private Long birthday;

      /**
     * 身高
     */
      @TableField("height")
    private Double height;

      /**
     * 体重
     */
      @TableField("weight")
    private Double weight;

      /**
     * 国家
     */
      @TableField("country")
    private String country;

      /**
     * 省份
     */
      @TableField("province")
    private String province;

      /**
     * 城市
     */
      @TableField("city")
    private String city;

    /**
     * 城市id
     */
    @TableField("city_id")
    private Integer cityId;
    /**
     * gps城市id
     */
    @TableField("gps_city_id")
    private Integer gpsCityId;

      /**
     * 昵称，支持emoji表情符
     */
      @TableField("nick_name")
    private String nickName;

      /**
     * 头像
     */
      @TableField("head_image")
    private String headImage;

      /**
     * app名字，0：Kieslect Fashion
     */
      @TableField("app_name")
    private Byte appName;

      /**
     * app系统，0：android，1：ios，2：harmony
     */
      @TableField("app_system")
    private Byte appSystem;

      /**
     * 手机型号
     */
      @TableField("app_type")
    private String phoneType;

      /**
     * app渠道，0：google，1：ios，2：huawei
     */
      @TableField("app_channel")
    private Integer appChannel;

      /**
     * app包状态，0：内测，1：公测，2：正式
     */
      @TableField("app_status")
    private Byte appStatus;

      /**
     * app版本
     */
      @TableField("app_version")
    private String appVersion;

    /**
     * 系统版本
     */
    @TableField("system_version")
    private String systemVersion;

      /**
     * 公英制，0：公制，1：英制
     */
      @TableField("metric_british")
    private Byte metricBritish;

      /**
     * 小时制，0：24小时制，1：12小时制
     */
      @TableField("hourly")
    private Byte hourly;

      /**
     * 温度单位，0：摄氏度，1：华氏度
     */
      @TableField("temperature")
    private Byte temperature;

      /**
     * 是否首次登录，0：否，1：是
     */
      @TableField("first_login")
    private Byte firstLogin;

      /**
     * 是否删除，0：否，1：是，2：注销超过7天永久删除
     */
      @TableField("del_status")
    private Byte delStatus;

      /**
     * 创建时间
     */
      @TableField("create_time")
    private Long createTime;

      /**
     * 更新时间
     */
      @TableField("update_time")
    private Long updateTime;

      /**
     * 步数目标
     */
      @TableField("steps_aim")
    private Integer stepsAim;

      /**
     * 活动时长目标
     */
      @TableField("activity_aim")
    private Integer activityAim;

      /**
     * 活动距离目标
     */
      @TableField("distance_aim")
    private Double distanceAim;

      /**
     * 卡路里目标
     */
      @TableField("calories_aim")
    private Double caloriesAim;

      /**
     * 睡眠时长目标
     */
      @TableField("sleep_aim")
    private Integer sleepAim;

    /**
     * 体重目标
     */
    @TableField("weight_aim")
    private Double weightAim;


    /**
     * ip地址
     */
    @TableField("ip_address")
    private String ipAddress;
}
