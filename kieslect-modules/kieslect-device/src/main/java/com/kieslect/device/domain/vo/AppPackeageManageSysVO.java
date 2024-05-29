package com.kieslect.device.domain.vo;

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
public class AppPackeageManageSysVO implements Serializable {

    private static final long serialVersionUID = 1L;

      private Integer id;

      /**
     * t_app_manage表的ID
     */
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
    private String version;

      /**
     * apk文件大小
     */
    private Double apkSize;

      /**
     * app开发内置的版本id
     */
    private String appVersion;

      /**
     * 升级内容
     */
    private String upgradeContent;

      /**
     * 创建时间
     */
    private Integer createTime;

      /**
     * 更新时间
     */
    private Integer updateTime;
}
