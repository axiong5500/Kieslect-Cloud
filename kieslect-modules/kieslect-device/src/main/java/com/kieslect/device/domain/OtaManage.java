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
 * @since 2024-08-02
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_ota_manage")
public class OtaManage implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 固件ID
     */
        @TableId(value = "ota_id", type = IdType.AUTO)
      private Long otaId;

      /**
     * 设备ID，见 t_device_manage 表 ID
     */
      @TableField("device_inner_id")
    private Long deviceInnerId;

      /**
     * 固件版本号
     */
      @TableField("ota_version")
    private String otaVersion;

      /**
     * 固件描述
     */
      @TableField("ota_desc")
    private String otaDesc;

      /**
     * 固件文件下载地址
     */
      @TableField("file_url")
    private String fileUrl;

      /**
     * 发布时间
     */
      @TableField("release_date")
    private Long releaseDate;

      /**
     * 状态，0：未激活，1：激活
     */
      @TableField("ota_status")
    private Integer otaStatus;

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
     * ota设计策略，0：不强制，1：强制
     */
      @TableField("ota_upgrade")
    private Integer otaUpgrade;

    /**
     * 手机版本，0：内测，1：公测，2：正式
     */
    @TableField("mode")
    private Integer mode;

    /**
     * 排序ID
     */
    @TableField("sort_id")
    private String sortId;
}
