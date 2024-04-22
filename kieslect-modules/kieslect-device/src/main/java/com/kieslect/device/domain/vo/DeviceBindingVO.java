package com.kieslect.device.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-04-12
 */
@Data
public class DeviceBindingVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
      private Integer bindingId;

    @JsonIgnore
    private Integer userId;


    private String mac;

    private Integer deviceId;

    private String form;

    private String deviceVersion;

    private Integer bindingStatus;

    private Long activeTime;

    @JsonIgnore
    private String createTime;

    @JsonIgnore
    private String updateTime;

}
