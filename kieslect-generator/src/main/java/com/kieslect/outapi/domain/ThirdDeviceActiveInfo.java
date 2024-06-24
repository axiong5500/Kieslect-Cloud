package com.kieslect.outapi.domain;

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
 * @since 2024-06-24
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_third_device_active_info")
public class ThirdDeviceActiveInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 设备ID
     */
      @TableField("device_id")
    private String deviceId;

      /**
     * 设备别名
     */
      @TableField("device_alias")
    private String deviceAlias;

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
     * 手机系统 (0: Android, 1: iOS, 2: 鸿蒙)
     */
      @TableField("operating_system")
    private String operatingSystem;

      /**
     * 手机型号
     */
      @TableField("phone_model")
    private String phoneModel;

      /**
     * 性别 (0: 男性, 1: 女性, 2: 其他)
     */
      @TableField("gender")
    private String gender;

      /**
     * 出生年月
     */
      @TableField("birthdate")
    private LocalDate birthdate;

      /**
     * 手表激活时间的UNIX时间戳
     */
      @TableField("activation_time")
    private Long activationTime;

      /**
     * 手表激活时区（UTC+8）
     */
      @TableField("activation_timezone")
    private String activationTimezone;

      /**
     * MAC地址
     */
      @TableField("mac_address")
    private String macAddress;

      /**
     * SN（序列号）
     */
      @TableField("serial_number")
    private String serialNumber;

      /**
     * IP地址
     */
      @TableField("ip_address")
    private String ipAddress;

      /**
     * 手表固件版本号
     */
      @TableField("firmware_version")
    private String firmwareVersion;

      /**
     * 数据来源 (0: 来源1, 1: 来源2, 2: 来源3)
     */
      @TableField("data_source")
    private String dataSource;

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
