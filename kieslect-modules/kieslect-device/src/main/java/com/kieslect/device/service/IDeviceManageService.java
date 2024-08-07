package com.kieslect.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.device.domain.DeviceManage;
import com.kieslect.device.domain.vo.DeviceManageVO;

import java.util.List;

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

    List<DeviceManageVO> getDeviceManageList();

    List<DeviceManageVO> getDeviceManageList(String deviceId, Integer integer, Integer producers);
}
