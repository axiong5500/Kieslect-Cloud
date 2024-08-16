package com.kieslect.device.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-08-09
 */
@Data
public class DialVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表盘形状，
     */
    private Integer dialShape;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;

    /**
     * 表盘透明底图地址
     */
    private String dialClearPhoto;

    /**
     * 表带图片地址
     */
    private String strapsPhoto;

    /**
     * 表盘列表
     */
    List<DialManageVO> dialList;
}
