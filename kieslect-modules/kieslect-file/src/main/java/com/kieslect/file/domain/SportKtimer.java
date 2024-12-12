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
  @TableName("t_sport_ktimer")
public class SportKtimer implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 每个计时器的唯一标识
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 用户ID，标识该计时器数据属于哪个用户
     */
      @TableField("user_id")
    private Long userId;

      /**
     * 当前时间与开始时间的差值，单位：秒
     */
      @TableField("total_time")
    private Integer totalTime;

      /**
     * 运动时长，单位：秒
     */
      @TableField("time")
    private Integer time;

      /**
     * 计时器状态（1-开始，2-暂停，3-停止）
     */
      @TableField("status")
    private Integer status;

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
