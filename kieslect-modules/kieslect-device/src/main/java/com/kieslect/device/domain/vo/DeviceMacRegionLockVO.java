package com.kieslect.device.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-11-15
 */
@Data
public class DeviceMacRegionLockVO implements Serializable {

    private static final long serialVersionUID = 1L;

      private Long id;

      /**
     * 设备ID，见 t_device_manage 表 ID
     */
    private Integer deviceId;

      /**
     * 设备mac
     */
    private String mac;

      /**
     * 国家代码列表
     */
    private String countryCodes;

      /**
     * 创建时间
     */
    private Long createTime;

      /**
     * 更新时间
     */
    private Long updateTime;

      /**
     * 锁类型（0：不可用国家，1：只限定国家可用）
     */
    private Integer lockType;

      /**
     * 策略ID，见 t_policy_info 表
     */
    private Integer policyId;

    private String strategyName;
}
