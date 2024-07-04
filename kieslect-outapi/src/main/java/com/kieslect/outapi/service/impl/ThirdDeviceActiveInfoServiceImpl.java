package com.kieslect.outapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.outapi.domain.ThirdDeviceActiveInfo;
import com.kieslect.outapi.mapper.ThirdDeviceActiveInfoMapper;
import com.kieslect.outapi.service.IThirdDeviceActiveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-06-24
 */
@Service
public class ThirdDeviceActiveInfoServiceImpl extends ServiceImpl<ThirdDeviceActiveInfoMapper, ThirdDeviceActiveInfo> implements IThirdDeviceActiveInfoService {

    @Autowired
    private ThirdDeviceActiveInfoMapper thirdDeviceActiveInfoMapper;

    @Override
    public boolean insertOrUpdateBatch(List<ThirdDeviceActiveInfo> records) {
        return thirdDeviceActiveInfoMapper.insertOrUpdateBatch(records);
    }
}
