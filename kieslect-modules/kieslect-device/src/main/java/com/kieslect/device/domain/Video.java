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
 * @since 2024-07-27
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_video")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 视频标题
     */
      @TableField("video_title")
    private String videoTitle;

      /**
     * 视频封面
     */
      @TableField("video_cover")
    private String videoCover;

      /**
     * 视频路径
     */
      @TableField("video_url")
    private String videoUrl;

      /**
     * 归属品牌id列表
     */
      @TableField("brand_type_ids")
    private String brandTypeIds;

      /**
     * 归属方案商id列表
     */
      @TableField("provider_type_ids")
    private String providerTypeIds;

      /**
     * 设备id列表，与方案商组合成唯一
     */
      @TableField("device_ids")
    private String deviceIds;

      /**
     * 手机机型，0：全部（默认），1：iPhone，2：Android
     */
      @TableField("phone_model")
    private Integer phoneModel;

      /**
     * 视频状态，1：上架（默认），2：下架
     */
      @TableField("status")
    private Integer status;

      /**
     * 语言，默认英语
     */
      @TableField("language")
    private Integer language;

      /**
     * 视频分类
     */
      @TableField("video_type")
    private String videoType;

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
     * 视频删除状态，0：激活，1：删除
     */
      @TableField("del_status")
    private Integer delStatus;
}
