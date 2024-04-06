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
  @TableName("t_app_description")
public class AppDescription implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId("id")
      private Integer id;

      /**
     * app id，见 t_app_manage 表 id
     */
      @TableField("app_id")
    private Integer appId;

      /**
     * 产品描述
     */
      @TableField("app_product_description")
    private String appProductDescription;

      /**
     * 语言版本，0：中文，1：英文
     */
      @TableField("language_version")
    private Integer languageVersion;

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
