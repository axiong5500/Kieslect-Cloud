package com.kieslect.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.device.domain.DeviceActiveInfo;
import com.kieslect.device.domain.ThirdUserInfo;

import java.util.List;

/**
 * <p>
 * 设备激活数据表 服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-08-21
 */
public interface IDeviceActiveInfoService extends IService<DeviceActiveInfo> {

    void insertIgnoreBatch(List<DeviceActiveInfo> deviceActiveInfoList);

    List<ThirdUserInfo> searchThirdUserInfoByUserId(Long kid);
}
