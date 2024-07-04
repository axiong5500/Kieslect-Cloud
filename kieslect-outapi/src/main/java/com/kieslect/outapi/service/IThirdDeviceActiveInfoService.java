package com.kieslect.outapi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.outapi.domain.ThirdDeviceActiveInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-06-24
 */
public interface IThirdDeviceActiveInfoService extends IService<ThirdDeviceActiveInfo> {

    boolean insertOrUpdateBatch(List<ThirdDeviceActiveInfo> records);
}
