package com.kieslect.device.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author kieslect
 * @since 2024-08-02
 */
@Data
public class OtaManageVO implements Serializable {

    private static final long serialVersionUID = 1L;


    private Long otaId;

    /**
     * 设备ID，见 t_device_manage 表 ID
     */
    private Long deviceInnerId;

    /**
     * 固件版本号
     */
    private String otaVersion;

    /**
     * 固件描述
     */
    private String otaDesc;

    /**
     * 固件文件下载地址
     */
    private String fileUrl;

    /**
     * 发布时间
     */
    private Long releaseDate;

    /**
     * 状态，0：未激活，1：激活
     */
    private Integer otaStatus;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * ota设计策略，0：不强制，1：强制
     */
    private Integer otaUpgrade;

    /**
     * 手机版本，0：内测，1：公测，2：正式
     */
    private Integer mode;

}
