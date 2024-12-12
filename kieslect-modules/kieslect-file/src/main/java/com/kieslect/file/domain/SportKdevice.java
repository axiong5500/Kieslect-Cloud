package com.kieslect.file.domain;

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
 * @since 2024-11-22
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_sport_kdevice")
public class SportKdevice implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 每个设备的唯一标识
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;


      /**
     * 用户ID，标识该设备属于哪个用户
     */
      @TableField("user_id")
    private Long userId;

      /**
     * 设备类型（0-手机，1-手表）
     */
      @TableField("device_type")
    private Integer deviceType;

      /**
     * 设备的MAC地址，用于唯一标识设备
     */
      @TableField("mac")
    private String mac;

      /**
     * 设备的名称（如设备型号）
     */
      @TableField("name")
    private String name;

      /**
     * 创建时间，Unix时间戳（秒）
     */
      @TableField("create_time")
    private Long createTime;

      /**
     * 更新时间，Unix时间戳（秒）
     */
      @TableField("update_time")
    private Long updateTime;
}
