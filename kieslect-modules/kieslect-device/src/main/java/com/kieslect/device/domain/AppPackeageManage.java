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
 * @since 2024-05-22
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_app_packeage_manage")
public class AppPackeageManage implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * t_app_manage表的ID
     */
      @TableField("app_id")
    private Integer appId;

      /**
     * 安装包文件URL
     */
      @TableField("package_download_url")
    private String packageDownloadUrl;

      /**
     * 应用系统，0：安卓，1：IOS
     */
      @TableField("apk_system")
    private String apkSystem;

      /**
     * 升级方式，0：不强制升级，1：强制升级
     */
      @TableField("update_status")
    private Byte updateStatus;

      /**
     * 发布渠道，0：Google，1：华为，2：腾讯
     */
      @TableField("channel")
    private String channel;

      /**
     * 弹窗提示，0：不提示，1：提示
     */
      @TableField("prompt")
    private Byte prompt;

      /**
     * 安装包名
     */
      @TableField("package_name")
    private String packageName;

      /**
     * 版本号
     */
      @TableField("version")
    private String version;

      /**
     * apk文件大小
     */
      @TableField("apk_size")
    private Double apkSize;

      /**
     * app开发内置的版本id
     */
      @TableField("app_version")
    private String appVersion;

      /**
     * 升级内容
     */
      @TableField("upgrade_content")
    private String upgradeContent;

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
