package com.kieslect.device.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-07-04
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_icon")
public class Icon implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    @TableField("code")
    private String code;

    @TableField("save_pack_name")
    private String savePackName;

    @TableField("ute_value")
    private Integer uteValue;

    @TableField("description")
    private String description;

    @TableField("param_name")
    private String paramName;

    @TableField("icon")
    private String icon;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
