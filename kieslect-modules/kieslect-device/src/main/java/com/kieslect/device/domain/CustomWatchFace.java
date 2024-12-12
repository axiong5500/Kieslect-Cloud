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
 * 自定义表盘表
 * </p>
 *
 * @author kieslect
 * @since 2024-12-09
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_custom_watch_face")
public class CustomWatchFace implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 主键ID
     */
        @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      /**
     * 字体默认颜色，例如 #FFFFFF
     */
      @TableField("color")
    private Integer color;

    /**
     * 背景图片URL
     */
    @TableField("font_url")
    private String fontUrl;

      /**
     * 背景图片URL
     */
      @TableField("background_url")
    private String backgroundUrl;

      /**
     * 表盘宽度
     */
      @TableField("width")
    private Integer width;

      /**
     * 表盘高度
     */
      @TableField("height")
    private Integer height;

      /**
     * 表盘类型，0：圆形，1：方形
     */
      @TableField("watch_type")
    private Byte watchType;

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
