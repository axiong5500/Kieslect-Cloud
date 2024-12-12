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
  @TableName("t_sport_kdistance")
public class SportKdistance implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 每条距离数据的唯一标识
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 用户ID，标识该距离数据属于哪个用户
     */
      @TableField("user_id")
    private Long userId;

      /**
     * 时间，单位：秒
     */
      @TableField("time")
    private Integer time;

      /**
     * 距离，单位：米
     */
      @TableField("distance")
    private Double distance;

      /**
     * 速度，单位：米/秒
     */
      @TableField("speed")
    private Double speed;

      /**
     * 圈数，记录总圈数
     */
      @TableField("lap")
    private Integer lap;

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
