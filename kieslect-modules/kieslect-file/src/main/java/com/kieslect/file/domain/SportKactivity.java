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
  @TableName("t_sport_kactivity")
public class SportKactivity implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 每条活动数据的唯一标识
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 用户ID，标识该活动数据属于哪个用户
     */
      @TableField("user_id")
    private Long userId;

      /**
     * 运动时长，单位：秒
     */
      @TableField("time")
    private Integer time;

      /**
     * 心率，单位：次/分钟
     */
      @TableField("hr")
    private Integer hr;

      /**
     * 步数
     */
      @TableField("steps")
    private Integer steps;

      /**
     * 步频，单位：步数/分钟
     */
      @TableField("cadence")
    private Integer cadence;

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
