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
  @TableName("t_app_manage")
public class AppManage implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * app名称
     */
      @TableField("app_name")
    private String appName;

      /**
     * app标识
     */
      @TableField("app_mark")
    private String appMark;

      /**
     * app logo地址
     */
      @TableField("app_logo")
    private String appLogo;

      /**
     * app下载链接集合id,多个参数用逗号隔开
     */
      @TableField("app_download_ids")
    private String appDownloadIds;

      /**
     * app产品描述id,多个参数用逗号隔开
     */
      @TableField("app_product_description")
    private String appProductDescription;

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
     * app状态
     */
      @TableField("app_status")
    private Byte appStatus;

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
}
