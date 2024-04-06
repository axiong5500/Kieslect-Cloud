package com.kieslect.device.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.device.domain.DeviceManage;
import com.kieslect.device.domain.ParamConfig;
import com.kieslect.device.mapper.DeviceManageMapper;
import com.kieslect.device.service.IDeviceManageService;
import com.kieslect.device.service.IParamConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-04-01
 */
@Service
public class DeviceManageServiceImpl extends ServiceImpl<DeviceManageMapper, DeviceManage> implements IDeviceManageService {

    @Autowired
    private DeviceManageMapper deviceManageMapper;
    @Autowired
    private IParamConfigService paramConfigService;



    @Override
    public void updateEntity(DeviceManage deviceManage) {
        DeviceManage soureEntity = getById(deviceManage.getId());
        if (StrUtil.isNotBlank(deviceManage.getParamIds())){
            String paramcollection = deviceManage.getParamCollection();
            List<ParamConfig> paramConfigs = paramConfigService.list();

        }
    }
}
