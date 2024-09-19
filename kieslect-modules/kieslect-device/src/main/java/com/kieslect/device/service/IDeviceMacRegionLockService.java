package com.kieslect.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.device.domain.DeviceMacRegionLock;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-09-06
 */
public interface IDeviceMacRegionLockService extends IService<DeviceMacRegionLock> {
    int getLock(Integer kId, String mac, Integer geoNameId);
}
