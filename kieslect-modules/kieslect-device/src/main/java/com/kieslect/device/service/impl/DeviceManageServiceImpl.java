package com.kieslect.device.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.device.domain.DeviceManage;
import com.kieslect.device.domain.ParamConfig;
import com.kieslect.device.domain.vo.ParamConfigVO;
import com.kieslect.device.mapper.DeviceManageMapper;
import com.kieslect.device.service.IDeviceManageService;
import com.kieslect.device.service.IParamConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            deviceManage.setParamIds("");
            deviceManage.setParamCollection("");
        }
        deviceManageMapper.updateById(deviceManage);
    }
}
