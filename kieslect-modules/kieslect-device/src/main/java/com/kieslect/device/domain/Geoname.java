package com.kieslect.device.domain;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2024-05-30
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_geoname")
public class Geoname implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private Long id;

    @TableField("geonameid")
    private Integer geonameid;

    @TableField("name")
    private String name;

    @TableField("asciiname")
    private String asciiname;

    @TableField("alternatenames")
    private String alternatenames;

    @TableField("latitude")
    private Double latitude;

    @TableField("longitude")
    private Double longitude;

    @TableField("feature_class")
    private String featureClass;

    @TableField("feature_code")
    private String featureCode;

    @TableField("country_code")
    private String countryCode;

    @TableField("cc2")
    private String cc2;

    @TableField("admin1_code")
    private String admin1Code;

    @TableField("admin2_code")
    private String admin2Code;

    @TableField("admin3_code")
    private String admin3Code;

    @TableField("admin4_code")
    private String admin4Code;

    @TableField("population")
    private Long population;

    @TableField("elevation")
    private Integer elevation;

    @TableField("dem")
    private Integer dem;

    @TableField("timezone")
    private String timezone;

    @TableField("modification_date")
    private String modificationDate;
}
