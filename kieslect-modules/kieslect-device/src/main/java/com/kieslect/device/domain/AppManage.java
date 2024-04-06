package com.kieslect.device.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

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
  @TableName("t_app_manage")
public class AppManage implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("id")
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
