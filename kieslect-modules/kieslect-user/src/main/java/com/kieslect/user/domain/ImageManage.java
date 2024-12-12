package com.kieslect.user.domain;

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
 * @since 2024-11-26
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_image_manage")
public class ImageManage implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 图片地址
     */
      @TableField("image_url")
    private String imageUrl;

    /**
     * 图片跳转地址
     */
    @TableField("redirect_url")
    private String redirectUrl;

      /**
     * 图片类型，1：广告位
     */
      @TableField("image_type")
    private Integer imageType;

      /**
     * app内部标识，0：kstyleos，1：ksos，2：ckos，3：ckwearos，4：kieslect，5：xofit，6：gloryfit，7：aiwearos
     */
      @TableField("app_name")
    private Integer appName;

      /**
     * 描述
     */
      @TableField("description")
    private String description;

      /**
     * 标题
     */
      @TableField("title")
    private String title;

      /**
     * 内容
     */
      @TableField("content")
    private String content;


      /**
     * 图片状态，1：上架，2：下架
     */
      @TableField("image_status")
    private Integer imageStatus;

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
