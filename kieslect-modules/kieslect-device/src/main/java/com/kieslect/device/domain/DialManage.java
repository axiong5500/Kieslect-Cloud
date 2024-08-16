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
 * @since 2024-08-09
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_dial_manage")
public class DialManage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表盘ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
      /**
     * 表盘ID
     */
      @TableField("dial_id")
      private Integer dialId;

      /**
     * 设备内部ID集合，见 t_device_manage 表 ID
     */
      @TableField("device_inner_id")
    private String deviceInnerId;

      /**
     * 自定义表盘规格表ID
     */
      @TableField("custome_specs_id")
    private String customeSpecsId;

      /**
     * 表盘名称
     */
      @TableField("dial_name")
    private String dialName;

      /**
     * 表盘类别
     */
      @TableField("dial_type")
    private String dialType;

      /**
     * 表盘图片url
     */
      @TableField("dial_image_url")
    private String dialImageUrl;

      /**
     * 表盘文件url
     */
      @TableField("dial_file_url")
    private String dialFileUrl;

      /**
     * 表盘文件大小
     */
      @TableField("dial_file_size")
    private Integer dialFileSize;

      /**
     * 表盘文件md5
     */
      @TableField("dial_file_md5")
    private String dialFileMd5;

      /**
     * 表盘描述
     */
      @TableField("dial_desc")
    private String dialDesc;

      /**
     * 作者
     */
      @TableField("author")
    private String author;

      /**
     * 表盘状态，0：下架，1：上架
     */
      @TableField("dial_status")
    private Integer dialStatus;

      /**
     * 手机版本，0：内测，1：公测，2：正式
     */
      @TableField("mode")
    private Integer mode;

      /**
     * 发布时间
     */
      @TableField("release_date")
    private Integer releaseDate;

      /**
     * 下架时间
     */
      @TableField("removal_date")
    private Integer removalDate;

      /**
     * 创建时间
     */
      @TableField("create_time")
    private Integer createTime;

      /**
     * 更新时间
     */
      @TableField("update_time")
    private Integer updateTime;
}
