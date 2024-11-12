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
 * @since 2024-11-12
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_device_mac_region_lock")
public class DeviceMacRegionLock implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 设备ID，见 t_device_manage 表 ID
     */
      @TableField("device_id")
    private Integer deviceId;

      /**
     * 设备mac
     */
      @TableField("mac")
    private String mac;

      /**
     * 国家代码列表
     */
      @TableField("country_codes")
    private String countryCodes;

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
     * 锁类型（0：不可用国家，1：只限定国家可用）
     */
      @TableField("lock_type")
    private Integer lockType;
}
