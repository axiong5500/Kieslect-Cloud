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
 * @since 2024-06-14
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_third_user_info")
public class ThirdUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 见 t_user_info 表 id
     */
      @TableField("user_id")
    private Long userId;

      /**
     * 第三方token
     */
      @TableField("third_token")
    private String thirdToken;

      /**
     * 第三方token类型，0：google，1：facebook，2：apple，3：wechat
     */
      @TableField("third_token_type")
    private Byte thirdTokenType;

      /**
     * 第三方token状态
     */
      @TableField("third_token_status")
    private Byte thirdTokenStatus;

      /**
     * 创建时间戳
     */
      @TableField("create_time")
    private Long createTime;

      /**
     * 更新时间戳
     */
      @TableField("update_time")
    private Long updateTime;

    @TableField("third_id")
    private String thirdId;

    @TableField("email")
    private String email;

    @TableField("verified_email")
    private String verifiedEmail;

    @TableField("name")
    private String name;

    @TableField("first_name")
    private String firstName;

    @TableField("last_name")
    private String lastName;

    @TableField("middle_name")
    private String middleName;

    @TableField("birthday")
    private String birthday;

    @TableField("gender")
    private String gender;

    @TableField("picture_url")
    private String pictureUrl;

    @TableField("profile_link")
    private String profileLink;

    @TableField("locale")
    private String locale;

    @TableField("timezone")
    private Integer timezone;

    @TableField("third_updated_time")
    private String thirdUpdatedTime;

    @TableField("app_name")
    private Byte appName;

    @TableField("local_picture_url")
    private String localPictureUrl;
}
