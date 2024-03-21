package com.kieslect.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-03-20
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
     * 生日
     */
      @TableField("birthday")
    private LocalDate birthday;

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
     * app名字
     */
      @TableField("app_name")
    private String appName;

      /**
     * app系统，0：android，1：ios，2：harmony
     */
      @TableField("app_system")
    private Byte appSystem;

      /**
     * app型号
     */
      @TableField("app_type")
    private String appType;

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
     * 是否删除，0：否，1：是
     */
      @TableField("del_status")
    private Byte delStatus;

      /**
     * 创建时间
     */
      @TableField("create_time")
    private LocalDateTime createTime;

      /**
     * 更新时间
     */
      @TableField("update_time")
    private LocalDateTime updateTime;
}
