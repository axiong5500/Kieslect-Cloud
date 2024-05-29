package com.kieslect.device.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 
 * </p>
 *
 * @author kieslect
 * @since 2024-04-28
 */
@Data
public class AppManageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * app名称
     */
    private String appName;

    /**
     * app标识
     */
    private String appMark;

    /**
     * app logo地址
     */
    private String appLogo;

    /**
     * 包管理
     */
    private List<AppPackeageManageVO> appPackages;


    /**
     * 配置表
     */
    private Map<String, Object> params = new HashMap<>();



    /**
     * 创建时间
     */
    private long createTime;

    /**
     * 更新时间
     */
    private long updateTime;
}
