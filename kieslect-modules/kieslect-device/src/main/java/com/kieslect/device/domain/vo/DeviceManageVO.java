package com.kieslect.device.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author kieslect
 * @since 2024-04-01
 */
@Data
public class DeviceManageVO {

    //内部型号(4)，厂商(4)，设备id(8)


    /**
     * 内部型号(4),对应id
     */
    private Integer innerId;

    /**
     * 设备id(8)，对应firmwareId固件id
     */
    private Integer deviceId;

    /**
     * 厂商(4),对应类别
     */
    private Integer producers;


    /**
     * 蓝牙名称
     */

    private String bluetoothName;

    /**
     * 设备分类，0：手表
     */
    @JsonIgnore
    private Integer deviceType;

    /**
     * 项目名称
     */
    @JsonIgnore
    private String projectName;

    /**
     * 固件id
     */
    @JsonIgnore
    private Integer firmwareId;

    /**
     * 产商，
     */
    @JsonIgnore
    private Integer form;

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
     * 表盘图片地址
     */

    private String dialPhoto;

    /**
     * 表带图片地址
     */

    private String strapsPhoto;


    /**
     * 产品描述
     */

    private String productDesc;


    /**
     * 参数设置值json集合
     */

    private Map<String, Object> params = new HashMap<>();

    /**
     * 创建时间
     */
    @JsonIgnore
    private String createTime;

    /**
     * 更新时间
     */
    @JsonIgnore
    private String updateTime;
}
