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
  @TableName("t_sport_klocation")
public class SportKlocation implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 每个定位数据的唯一标识
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 用户ID，标识该定位数据属于哪个用户
     */
      @TableField("user_id")
    private Long userId;

      /**
     * 记录时间，单位：秒（Unix时间戳）
     */
      @TableField("time")
    private Integer time;

      /**
     * 纬度，地理位置的纬度值
     */
      @TableField("latitude")
    private Double latitude;

      /**
     * 经度，地理位置的经度值
     */
      @TableField("longitude")
    private Double longitude;

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
