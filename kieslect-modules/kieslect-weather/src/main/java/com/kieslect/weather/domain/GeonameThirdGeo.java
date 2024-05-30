package com.kieslect.weather.domain;

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
 * @since 2024-05-30
 */
@Getter
@Setter
  @Accessors(chain = true)
  @TableName("t_geoname_third_geo")
public class GeonameThirdGeo implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      /**
     * t_geoname 表 geonameid 字段
     */
      @TableField("geonameid")
    private Integer geonameid;

      /**
     * 纬度
     */
      @TableField("latitude")
    private String latitude;

      /**
     * 经度
     */
      @TableField("longitude")
    private String longitude;

      /**
     * 来源，0：和风
     */
      @TableField("source_type")
    private Integer sourceType;

      /**
     * 请求URL
     */
      @TableField("request_url")
    private String requestUrl;

      /**
     * 响应体
     */
      @TableField("source_response")
    private String sourceResponse;

    @TableField("create_time")
    private Long createTime;

    @TableField("update_time")
    private Long updateTime;

      /**
     * 请求体
     */
      @TableField("request_body")
    private String requestBody;
}
