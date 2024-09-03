package com.kieslect.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.device.domain.DeviceActiveInfo;
import com.kieslect.device.domain.ThirdUserInfo;
import com.kieslect.device.mapper.DeviceActiveInfoMapper;
import com.kieslect.device.service.IDeviceActiveInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 设备激活数据表 服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-08-21
 */
@Service
public class DeviceActiveInfoServiceImpl extends ServiceImpl<DeviceActiveInfoMapper, DeviceActiveInfo> implements IDeviceActiveInfoService {

    @Autowired
    private DeviceActiveInfoMapper deviceActiveInfoMapper;
    @Override
    public void insertIgnoreBatch(List<DeviceActiveInfo> deviceActiveInfoList) {
        deviceActiveInfoMapper.insertIgnoreBatch(deviceActiveInfoList);
    }

    @Override
    public List<ThirdUserInfo> searchThirdUserInfoByUserId(Long kid) {
        return deviceActiveInfoMapper.searchThirdUserInfoByUserId(kid);
    }
}
