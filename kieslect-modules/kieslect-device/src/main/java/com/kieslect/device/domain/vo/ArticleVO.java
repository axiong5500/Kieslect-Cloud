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
 * @since 2024-04-29
 */
@Data
public class ArticleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * appId
     */
    @JsonIgnore
    private Integer appId;

    /**
     * 语言
     */
    private Integer language;

}
