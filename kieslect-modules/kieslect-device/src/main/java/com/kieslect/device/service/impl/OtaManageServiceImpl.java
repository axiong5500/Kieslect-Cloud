package com.kieslect.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.device.domain.OtaManage;
import com.kieslect.device.mapper.OtaManageMapper;
import com.kieslect.device.service.IOtaManageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-08-02
 */
@Service
public class OtaManageServiceImpl extends ServiceImpl<OtaManageMapper, OtaManage> implements IOtaManageService {

    @Override
    public List<OtaManage> getOtaByDeviceInnerIdAndVersion(Long deviceInnerId, String otaVersion) {
        LambdaQueryWrapper<OtaManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OtaManage::getDeviceInnerId, deviceInnerId);

        if (otaVersion != null && !otaVersion.isEmpty()) {
            queryWrapper.gt(OtaManage::getOtaVersion, otaVersion);
        }
        queryWrapper.orderByDesc(OtaManage::getReleaseDate);

        return this.list(queryWrapper);
    }
}
