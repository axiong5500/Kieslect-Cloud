package com.kieslect.device.domain;

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
 * @since 2024-04-20
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_device_binding")
public class DeviceBinding implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 用户id
     */
      @TableField("user_id")
    private Integer userId;

      /**
     * mac地址
     */
      @TableField("mac")
    private String mac;

      /**
     * 产商
     */
      @TableField("form")
    private String form;

      /**
     * 设备版本号
     */
      @TableField("device_version")
    private String deviceVersion;

    @TableField("create_time")
    private String createTime;

    @TableField("update_time")
    private String updateTime;

      /**
     * 绑定状态，0：未绑定，1：已绑定
     */
      @TableField("binding_status")
    private Integer bindingStatus;

      /**
     * 激活时间
     */
      @TableField("active_time")
    private Long activeTime;

    /**
     * 激活时间
     */
    @TableField("device_id")
    private String deviceId;

    /**
     * 设备密码
     */
    @TableField("device_password")
    private String devicePassword;
}
