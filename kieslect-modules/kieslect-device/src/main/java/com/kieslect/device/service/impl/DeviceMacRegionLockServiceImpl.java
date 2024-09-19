package com.kieslect.device.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.device.domain.DeviceMacRegionLock;
import com.kieslect.device.domain.Geoname;
import com.kieslect.device.mapper.DeviceMacRegionLockMapper;
import com.kieslect.device.service.IDeviceMacRegionLockService;
import com.kieslect.device.service.IGeonameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-09-06
 */
@Service
public class DeviceMacRegionLockServiceImpl extends ServiceImpl<DeviceMacRegionLockMapper, DeviceMacRegionLock> implements IDeviceMacRegionLockService {

    @Autowired
    private DeviceMacRegionLockMapper deviceMacRegionLockMapper;

    @Autowired
    private IGeonameService geoNameService;


    @Override
    public int getLock(Integer kId, String mac, Integer geoNameId) {
        if (StrUtil.isNotBlank(mac)) {
            LambdaQueryWrapper<DeviceMacRegionLock> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DeviceMacRegionLock::getMac, mac);
            if (deviceMacRegionLockMapper.selectCount(queryWrapper) == 0) {
                return 1;
            }
        }

        LambdaQueryWrapper<Geoname> geoNameLambdaQueryWrapper = new LambdaQueryWrapper<>();
        geoNameLambdaQueryWrapper.eq(Geoname::getGeonameid, geoNameId);
        String countryCode = geoNameService.getOne(geoNameLambdaQueryWrapper).getCountryCode();

        LambdaQueryWrapper<DeviceMacRegionLock> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DeviceMacRegionLock::getMac, mac);
        queryWrapper.like(DeviceMacRegionLock::getCountryCodes, countryCode);
        return Math.toIntExact(deviceMacRegionLockMapper.selectCount(queryWrapper));
    }
}
