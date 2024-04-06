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
 * @since 2024-04-01
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_param_config")
public class ParamConfig implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(type = IdType.AUTO)
      private Integer id;

      /**
     * 属性名称
     */
      @TableField("param_name")
    private String paramName;

      /**
     * 属性标题
     */
      @TableField("param_title")
    private String paramTitle;

      /**
     * 属性描述
     */
      @TableField("param_desc")
    private String paramDesc;

      /**
     * 属性默认值
     */
      @TableField("param_value")
    private String paramValue;

      /**
     * 模块类型，0：心率模块，1：心电模块，2：升级
     */
      @TableField("param_type")
    private Integer paramType;

      /**
     * 模块分组，0：设备模块， 1：app模块
     */
      @TableField("param_group")
    private Integer paramGroup;

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
