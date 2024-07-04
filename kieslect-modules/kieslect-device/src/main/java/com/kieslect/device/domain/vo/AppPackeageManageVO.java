package com.kieslect.device.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-05-22
 */
@Data
public class AppPackeageManageVO implements Serializable {

    private static final long serialVersionUID = 1L;


      /**
     * t_app_manage表的ID
     */
      @JsonIgnore
    private Integer appId;

      /**
     * 安装包文件URL
     */
    private String packageDownloadUrl;

      /**
     * 应用系统，0：安卓，1：IOS
     */
    private String apkSystem;

      /**
     * 升级方式，0：不强制升级，1：强制升级
     */
    private Byte updateStatus;

      /**
     * 发布渠道，0：Google，1：华为，2：腾讯
     */
    private String channel;

      /**
     * 弹窗提示，0：不提示，1：提示
     */
    private Byte prompt;

      /**
     * 安装包名
     */
    private String packageName;

      /**
     * 版本号
     */
      @JsonIgnore
    private String version;

    private Integer versionCode;

      /**
     * apk文件大小
     */
    private Double apkSize;

      /**
     * app开发内置的版本id
     */
      @JsonIgnore
    private String appVersion;

    private String versionName;

      /**
     * 升级内容
     */
    private String upgradeContent;

}
