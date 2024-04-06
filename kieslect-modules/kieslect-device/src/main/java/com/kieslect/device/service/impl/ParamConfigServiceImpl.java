package com.kieslect.device.service.impl;

import com.kieslect.device.domain.ParamConfig;
import com.kieslect.device.domain.vo.ParamConfigVO;
import com.kieslect.device.mapper.ParamConfigMapper;
import com.kieslect.device.service.IParamConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-04-01
 */
@Service
public class ParamConfigServiceImpl extends ServiceImpl<ParamConfigMapper, ParamConfig> implements IParamConfigService {

    @Autowired
    private ParamConfigMapper paramConfigMapper;
    @Override
    public Object getList(ParamConfigVO paramConfigVO) {
        return paramConfigMapper.getList(paramConfigVO);
    }
}
