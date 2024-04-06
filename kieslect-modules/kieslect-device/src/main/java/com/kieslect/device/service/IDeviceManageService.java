package com.kieslect.device.service;

import com.kieslect.device.domain.DeviceManage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-04-01
 */
public interface IDeviceManageService extends IService<DeviceManage> {

    void updateEntity(DeviceManage deviceManage);
}
