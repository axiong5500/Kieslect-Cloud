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
  @TableName("t_app_download")
public class AppDownload implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("id")
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
