package com.kieslect.device.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.device.domain.DeviceMacRegionLock;
import com.kieslect.device.domain.Geoname;
import com.kieslect.device.domain.vo.DeviceMacRegionLockVO;
import com.kieslect.device.mapper.DeviceMacRegionLockMapper;
import com.kieslect.device.service.IDeviceMacRegionLockService;
import com.kieslect.device.service.IGeonameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        // 判断mac在不在不可用区域表中，如果不存在就说明全球可用返回1
        try {
            System.out.println(deviceMacRegionLockMapper.selectList(null));
            DeviceMacRegionLock deviceMacRegionLock = null;
            if (StrUtil.isNotBlank(mac)) {
                LambdaQueryWrapper<DeviceMacRegionLock> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(DeviceMacRegionLock::getMac, mac);
                deviceMacRegionLock = deviceMacRegionLockMapper.selectOne(queryWrapper);
                if (deviceMacRegionLock == null){
                    return 1;
                }
            }
            String countryCodes = deviceMacRegionLock.getCountryCodes();
            Integer locktype = deviceMacRegionLock.getLockType();

            // 如果mac在锁区区域表里，那么判断它的锁区类型，如果为0，那么返回0，为1，返回1
            LambdaQueryWrapper<Geoname> geoNameLambdaQueryWrapper = new LambdaQueryWrapper<>();
            geoNameLambdaQueryWrapper.eq(Geoname::getGeonameid, geoNameId);
            String countryCode = geoNameService.getOne(geoNameLambdaQueryWrapper).getCountryCode();

            boolean contains = countryCodes.toLowerCase().contains(countryCode.toLowerCase());
            if (contains && locktype == 0){
                return 0;
            }else if (contains && locktype == 1){
                return 1;
            }
            return 1;
        }catch (Exception e){
            // 防止数据库出现两笔相同的mac地址，导致程序崩溃
            return 1;
        }
    }

    @Override
    public Page<DeviceMacRegionLockVO> getDeviceMacRegionLockWithPolicy(DeviceMacRegionLock deviceMacRegionLock, int pageNum, int pageSize) {
        // 将 mac 字符串分割为列表
        List<String> macList = null;
        if (deviceMacRegionLock != null && StrUtil.isNotEmpty(deviceMacRegionLock.getMac())) {
            macList = StrUtil.split(deviceMacRegionLock.getMac(), ",");
        }
        // 创建分页对象
        Page<DeviceMacRegionLockVO> page = new Page<>(pageNum, pageSize);
        return deviceMacRegionLockMapper.getDeviceMacRegionLockWithPolicy(page,macList);
    }
}
