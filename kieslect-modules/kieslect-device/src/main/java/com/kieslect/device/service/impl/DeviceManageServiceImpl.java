package com.kieslect.device.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.device.domain.DeviceManage;
import com.kieslect.device.domain.ParamConfig;
import com.kieslect.device.domain.vo.DeviceManageVO;
import com.kieslect.device.domain.vo.ParamConfigVO;
import com.kieslect.device.mapper.DeviceManageMapper;
import com.kieslect.device.service.IDeviceManageService;
import com.kieslect.device.service.IParamConfigService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-04-01
 */
@Service
public class DeviceManageServiceImpl extends ServiceImpl<DeviceManageMapper, DeviceManage> implements IDeviceManageService {

    @Autowired
    private DeviceManageMapper deviceManageMapper;
    @Autowired
    private IParamConfigService paramConfigService;

    @Override
    public List<DeviceManageVO> getDeviceManageList() {
        // 获取所有paramConfig
        List<ParamConfig> paramConfigList = paramConfigService.list();
        Map<String, String> paramConfigMap = new HashMap<>();
        for (ParamConfig paramConfig : paramConfigList) {
            paramConfigMap.put(String.valueOf(paramConfig.getId()), paramConfig.getParamName());
        }

        List<DeviceManage> list = this.list();
        List<DeviceManageVO> result = new ArrayList<>();
        for (DeviceManage deviceManage : list) {
            // 创建新的 DeviceManageVO 对象，将 DeviceManage 对象的属性复制到 DeviceManageVO
            DeviceManageVO deviceManageVO = new DeviceManageVO();
            BeanUtils.copyProperties(deviceManage, deviceManageVO);

            Map<String, Object> updatedParamCollection = new HashMap<>();
            // 添加其他属性复制...
            Map<String, Object> paramCollectionJson = JSONUtil.toBean(deviceManage.getParamCollection(), Map.class);
            paramCollectionJson.forEach((k, v) -> {
                if (paramConfigMap.containsKey(k)) {
                    updatedParamCollection.put(paramConfigMap.get(k), v);
                }
            });
            deviceManageVO.setParams(updatedParamCollection);

            deviceManageVO.setDeviceId(deviceManage.getFirmwareId());
            deviceManageVO.setProducers(deviceManage.getForm());
            deviceManageVO.setInnerId(deviceManage.getId());

            // 将转换后的 DeviceManageVO 对象添加到 result 列表
            result.add(deviceManageVO);
        }
        return result;
    }

    @Override
    public List<DeviceManageVO> getDeviceManageList(String deviceId, Integer producers, Integer appName) {
        // 获取所有paramConfig
        List<ParamConfig> paramConfigList = paramConfigService.list();
        Map<String, String> paramConfigMap = new HashMap<>();
        for (ParamConfig paramConfig : paramConfigList) {
            paramConfigMap.put(String.valueOf(paramConfig.getId()), paramConfig.getParamName());
        }

        LambdaQueryWrapper<DeviceManage> queryWrapper = new LambdaQueryWrapper<>();
        if (deviceId != null) {
            queryWrapper.eq(DeviceManage::getFirmwareId, deviceId);
        }
        if (producers != null) {
            queryWrapper.eq(DeviceManage::getForm, producers);
        }
        if (appName != null) {
            queryWrapper.like(DeviceManage::getAppIds, appName);
        }

        List<DeviceManage> list = this.list(queryWrapper);
        List<DeviceManageVO> result = new ArrayList<>();
        for (DeviceManage deviceManage : list) {
            // 创建新的 DeviceManageVO 对象，将 DeviceManage 对象的属性复制到 DeviceManageVO
            DeviceManageVO deviceManageVO = new DeviceManageVO();
            BeanUtils.copyProperties(deviceManage, deviceManageVO);

            Map<String, Object> updatedParamCollection = new HashMap<>();
            // 添加其他属性复制...
            if (StrUtil.isNotBlank(deviceManage.getParamCollection())){
                Map<String, Object> paramCollectionJson = JSONUtil.toBean(deviceManage.getParamCollection(), Map.class);
                paramCollectionJson.forEach((k, v) -> {
                    if (paramConfigMap.containsKey(k)) {
                        updatedParamCollection.put(paramConfigMap.get(k), v);
                    }
                });
            }

            deviceManageVO.setParams(updatedParamCollection);

            deviceManageVO.setDeviceId(deviceManage.getFirmwareId());
            deviceManageVO.setProducers(deviceManage.getForm());
            deviceManageVO.setInnerId(deviceManage.getId());

            // 将转换后的 DeviceManageVO 对象添加到 result 列表
            result.add(deviceManageVO);
        }
        return result;
    }

    @Override
    public void updateEntity(DeviceManage deviceManage) {
        DeviceManage soureEntity = getById(deviceManage.getId());
        if (StrUtil.isNotBlank(deviceManage.getParamIds())) {

            Map<String, Object> resultMap = new HashMap<>();

            // 获取所有参数的默认值
            Map<String, String> paramMaps = new HashMap<>();
            List<ParamConfig> paramterconfigs = (List<ParamConfig>) paramConfigService.getList(new ParamConfigVO());
            paramterconfigs.forEach(paramConfig -> paramMaps.put(String.valueOf(paramConfig.getId()), paramConfig.getParamValue()));

            // 获得旧的参数集合，已经设置过的值
            String paramcollection = soureEntity.getParamCollection();
            JSONObject jsonCollection = null;
            if (StrUtil.isNotBlank(paramcollection)) {
                jsonCollection = JSONUtil.parseObj(paramcollection);
            }

            // 得到已经勾选的参数id
            String[] paramIds = deviceManage.getParamIds().split(",");
            for (String paramId : paramIds) {
                resultMap.put(paramId, paramMaps.get(paramId));
                if (jsonCollection != null) {
                    if (jsonCollection.containsKey(paramId)) {
                        resultMap.put(paramId, jsonCollection.get(paramId));
                    }
                }
            }

            deviceManage.setParamCollection(JSONUtil.toJsonStr(resultMap));

        } else {
            if (StrUtil.isBlank(deviceManage.getParamIds()) && StrUtil.isBlank( deviceManage.getParamCollection())){
                deviceManage.setParamIds("");
                deviceManage.setParamCollection("");
            }
        }
        deviceManageMapper.updateById(deviceManage);
    }


}
