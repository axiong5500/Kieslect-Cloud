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
  @TableName("t_sport_detail")
public class SportDetail implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 每条运动记录的唯一标识
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 用户ID，标识此运动记录属于哪个用户
     */
      @TableField("user_id")
    private Long userId;

      /**
     * 唯一标识，用于区分同一用户的不同运动记录
     */
      @TableField("only")
    private Long only;

      /**
     * 运动类型，定义不同的运动种类
     */
      @TableField("type")
    private Integer type;

    /**
     * 时区偏移量
     */
    @TableField("offset_hours")
    private Double offsetHours;

      /**
     * 运动开始时间，单位：秒（Unix时间戳）
     */
      @TableField("start_time")
    private Long startTime;

      /**
     * 运动结束时间，单位：秒（Unix时间戳）
     */
      @TableField("end_time")
    private Long endTime;

      /**
     * 运动时长，单位：秒
     */
      @TableField("time")
    private Integer time;

      /**
     * 总步数
     */
      @TableField("steps")
    private Integer steps;

      /**
     * 总路程，单位：米
     */
      @TableField("distance")
    private Double distance;

      /**
     * 最大速度，单位：米/秒
     */
      @TableField("max_speed")
    private Double maxSpeed;

      /**
     * 平均心率
     */
      @TableField("avg_hr")
    private Double avgHr;

      /**
     * 最大心率
     */
      @TableField("max_hr")
    private Integer maxHr;

      /**
     * 最小心率
     */
      @TableField("min_hr")
    private Integer minHr;

      /**
     * 设备类型（0-手机，1-手表）
     */
      @TableField("device_type")
    private Integer deviceType;

      /**
     * 目标数据，如目标距离、时间等
     */
      @TableField("aim_data")
    private Double aimData;

      /**
     * 目标类型（0-无效，1-时间，2-距离，3-卡路里，4-步数）
     */
      @TableField("aim_type")
    private Integer aimType;

      /**
     * 消耗的热量，单位：千卡
     */
      @TableField("calories")
    private Double calories;

      /**
     * 圈数，记录运动的总圈数
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
