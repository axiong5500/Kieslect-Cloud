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
 * 策略信息
 * </p>
 *
 * @author kieslect
 * @since 2024-11-14
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_policy_info")
public class PolicyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 策略名称
     */
      @TableField("strategy_name")
    private String strategyName;

      /**
     * 策略国家或地区
     */
      @TableField("country")
    private String country;

      /**
     * 策略状态，0：不可用，1：可用
     */
      @TableField("strategy_status")
    private Integer strategyStatus;

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
}
