package com.kieslect.user.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-07-04
 */
@Data
public class IconVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String code;

    private String savePackName;

    private Integer uteValue;

    @JsonProperty("desc")
    private String description;

    private String paramName;

    private String icon;

}
