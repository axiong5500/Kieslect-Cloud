package com.kieslect.device.domain;

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
 * @since 2024-04-01
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_device_manage")
public class DeviceManage implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("id")
      private Integer id;

      /**
     * 蓝牙名称
     */
      @TableField("bluetooth_name")
    private String bluetoothName;

      /**
     * 设备分类，0：手表
     */
      @TableField("device_type")
    private Integer deviceType;

      /**
     * 项目名称
     */
      @TableField("project_name")
    private String projectName;

      /**
     * 固件id
     */
      @TableField("firmware_id")
    private Integer firmwareId;

      /**
     * 类别，
     */
      @TableField("form")
    private Integer form;

      /**
     * 表盘形状，
     */
      @TableField("dial_shape")
    private Integer dialShape;

      /**
     * 宽度
     */
      @TableField("width")
    private Integer width;

      /**
     * 高度
     */
      @TableField("height")
    private Integer height;

      /**
     * 表盘图片地址
     */
      @TableField("dial_photo")
    private String dialPhoto;

      /**
     * 表带图片地址
     */
      @TableField("straps_photo")
    private String strapsPhoto;

      /**
     * 指针表盘文件
     */
      @TableField("point_dial_file")
    private String pointDialFile;

      /**
     * 数字表盘文件
     */
      @TableField("etc_dial_file")
    private String etcDialFile;

      /**
     * 产品描述
     */
      @TableField("product_desc")
    private String productDesc;

      /**
     * 3D文件
     */
      @TableField("3d_file")
    private String dFile;

      /**
     * 勾选参数集合，多个参数用逗号隔开
     */
      @TableField("param_ids")
    private String paramIds;

      /**
     * 参数设置值json集合
     */
      @TableField("param_collection")
    private String paramCollection;

      /**
     * 创建时间
     */
      @TableField("create_time")
    private String createTime;

      /**
     * 更新时间
     */
      @TableField("update_time")
    private String updateTime;
}
