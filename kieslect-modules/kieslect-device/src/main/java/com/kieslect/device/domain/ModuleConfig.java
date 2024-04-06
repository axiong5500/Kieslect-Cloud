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
 * @since 2024-04-05
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_module_config")
public class ModuleConfig implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * 父节点
     */
      @TableField("father_id")
    private Integer fatherId;

      /**
     * 模块名称
     */
      @TableField("module_name")
    private String moduleName;

      /**
     * 是否启用，0：否，1：是
     */
      @TableField("enable")
    private Byte enable;

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
