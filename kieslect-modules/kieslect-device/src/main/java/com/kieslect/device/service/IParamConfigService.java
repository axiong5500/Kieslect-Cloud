package com.kieslect.device.service;

import com.kieslect.device.domain.ParamConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.device.domain.vo.ParamConfigVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-04-01
 */
public interface IParamConfigService extends IService<ParamConfig> {

    Object getList(ParamConfigVO paramConfigVO);
}
