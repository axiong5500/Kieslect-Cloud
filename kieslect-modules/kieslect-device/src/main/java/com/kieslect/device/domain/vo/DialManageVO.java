package com.kieslect.device.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-08-09
 */
@Data
public class DialManageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 表盘ID
     */
    private Integer dialId;

    /**
     * 关联外部Id
     */
    private Integer relationId;

      /**
     * 表盘名称
     */
    private String dialName;


      /**
     * 表盘图片url
     */
    private String dialImageUrl;

      /**
     * 表盘文件url
     */
    private String dialFileUrl;

      /**
     * 表盘文件大小
     */
    private Integer dialFileSize;


      /**
     * 表盘描述
     */
    private String dialDesc;

      /**
     * 作者
     */
    private String author;

      /**
     * 表盘状态，0：下架，1：上架
     */
    private Integer dialStatus;

      /**
     * 手机版本，0：内测，1：公测，2：正式
     */
    private Integer mode;

    /**
     * 发布时间
     */
    private Integer releaseDate;

    /**
     * 创建时间
     */
    private Integer createTime;

    /**
     * 更新时间
     */
    private Integer updateTime;

    /**
     * 表盘下载量
     */
    private Integer downloadCount;

}
