package com.kieslect.device.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.device.domain.OtaManage;
import com.kieslect.device.mapper.OtaManageMapper;
import com.kieslect.device.service.IOtaManageService;
import org.springframework.stereotype.Service;

import java.util.Iterator;
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
        filterOtaManageList(list);

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
        queryWrapper.orderByAsc(OtaManage::getSortId);

        return queryWrapper;
    }

    /**
     * 处理结果，将 otaVersion 前加上 "V"，并过滤掉 otaUpgrade == 0 的元素
     * @param list
     */
    private void filterOtaManageList(List<OtaManage> list) {
        // 判断列表是否为空，避免空指针异常
        if (list == null || list.isEmpty()) {
            return;
        }

        // 获取列表最后一个元素
        OtaManage lastElement = list.get(list.size() - 1);

        // 使用迭代器遍历列表，安全地移除元素
        Iterator<OtaManage> iterator = list.iterator();

        while (iterator.hasNext()) {
            OtaManage otaManage = iterator.next();

            // 如果当前元素的 otaUpgrade == 0 且不是最后一个元素，则移除
            if (otaManage.getOtaUpgrade() == 0 && otaManage != lastElement) {
                iterator.remove(); // 安全地移除元素
            } else {
                // 更新剩余元素的版本号
                otaManage.setOtaVersion("V" + otaManage.getOtaVersion());
            }
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
