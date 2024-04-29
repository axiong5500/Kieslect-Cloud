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
 * @since 2024-04-28
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_app_download")
public class AppDownload implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * app id，见 t_app_manage 表 id
     */
      @TableField("app_id")
    private Integer appId;

      /**
     * app下载地址
     */
      @TableField("app_download_link")
    private String appDownloadLink;

      /**
     * app渠道，0：google，1：ios，2：huawei
     */
      @TableField("app_channel")
    private String appChannel;

      /**
     * app发布版本号
     */
      @TableField("version")
    private String version;

      /**
     * 包名
     */
      @TableField("package_name")
    private String packageName;

      /**
     * app内部版本号
     */
      @TableField("app_internal_version")
    private String appInternalVersion;

      /**
     * 创建时间
     */
      @TableField("create_time")
    private long createTime;

      /**
     * 更新时间
     */
      @TableField("update_time")
    private long updateTime;

      /**
     * app下载链接状态
     */
      @TableField("app_download_status")
    private Byte appDownloadStatus;
}
