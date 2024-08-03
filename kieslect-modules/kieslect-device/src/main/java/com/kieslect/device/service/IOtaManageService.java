package com.kieslect.device.service;

import com.kieslect.device.domain.OtaManage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-08-02
 */
public interface IOtaManageService extends IService<OtaManage> {

    List<OtaManage> getOtaByDeviceInnerIdAndVersion(Long deviceInnerId, String otaVersion);
}
