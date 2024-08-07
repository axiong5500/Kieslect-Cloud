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
        // 规范化版本号
        otaVersion = normalizeVersion(otaVersion);

        // 创建查询条件
        LambdaQueryWrapper<OtaManage> queryWrapper = createQueryWrapper(deviceInnerId, otaVersion);

        // 执行查询并获取结果
        List<OtaManage> list = this.list(queryWrapper);

        // 处理结果，将 otaVersion 前加上 "V"
        addVersionPrefix(list);

        return list;
    }

    /**
     * 创建查询条件的封装方法
     *
     * @param deviceInnerId 设备内码
     * @param otaVersion 版本号
     * @return LambdaQueryWrapper 查询条件
     */
    private LambdaQueryWrapper<OtaManage> createQueryWrapper(Long deviceInnerId, String otaVersion) {
        LambdaQueryWrapper<OtaManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OtaManage::getDeviceInnerId, deviceInnerId);

        if (otaVersion != null && !otaVersion.isEmpty()) {
            queryWrapper.gt(OtaManage::getOtaVersion, otaVersion);
        }
        queryWrapper.orderByDesc(OtaManage::getReleaseDate);

        return queryWrapper;
    }

    /**
     * 为列表中的每个 OtaManage 对象的 otaVersion 字段加上 "V"
     *
     * @param list OtaManage 对象列表
     */
    private void addVersionPrefix(List<OtaManage> list) {
        for (OtaManage otaManage : list) {
            otaManage.setOtaVersion("V" + otaManage.getOtaVersion());
        }
    }

    /**
     * 规范化版本号
     *
     * @param version 版本号
     * @return 规范化后的版本号
     */
    private String normalizeVersion(String version) {
        if (version != null) {
            version = version.toUpperCase().replace("V", "");
        }
        return version;
    }
}
