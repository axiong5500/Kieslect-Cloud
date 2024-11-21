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
 * @since 2024-11-14
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_country_mapping")
public class CountryMapping implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

    @TableField("country_code")
    private String countryCode;

    @TableField("country_name")
    private String countryName;

    @TableField("chinese_name")
    private String chineseName;

    @TableField("continent")
    private String continent;
}
