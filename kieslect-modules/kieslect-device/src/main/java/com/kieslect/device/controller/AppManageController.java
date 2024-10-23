package com.kieslect.device.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.AppDownload;
import com.kieslect.device.domain.AppManage;
import com.kieslect.device.domain.AppPackeageManage;
import com.kieslect.device.domain.ParamConfig;
import com.kieslect.device.domain.vo.AppManageVO;
import com.kieslect.device.domain.vo.AppPackeageManageVO;
import com.kieslect.device.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-04-01
 */
@RestController
@RequestMapping("/appManage")
public class AppManageController {
    @Autowired
    IAppManageService appManageService;

    @Autowired
    IAppPackeageManageService appPackeageManageService;

    @Autowired
    IArticleService articleService;

    @Autowired
    IParamConfigService paramConfigService;

    @Autowired
    IAppDownloadService appDownloadService;

    @GetMapping("/getQRList")
    public R<?> getQRList(@RequestParam(value = "appName", required = false) Integer appName) {
        List<Map<String, Object>> result = new ArrayList<>();

        LambdaQueryWrapper<AppManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(AppManage::getUserAppName);
        if (appName != null){
            queryWrapper.eq(AppManage::getUserAppName, appName);
        }
        List<AppManage> appManageList = appManageService.list(queryWrapper);

        if (appManageList == null || appManageList.size() == 0) {
            Map<String, Object> map = new HashMap<>();
            map.put("appNameSign", appName);
            map.put("appQRLink", "");
            result.add(map);
            return R.ok(result);
        }

        String[] appMarkList = {"KSTYLEOS", "CKOS", "CKWEAROS"};
        for (int i = 0; i < appManageList.size(); i++) {
            Map<String, Object> map = new HashMap<>();

            String appMark = appManageList.get(i).getAppMark(); // 获取当前的 appMark
            map.put("appNameSign", appManageList.get(i).getUserAppName());

            // 检查 appMark 是否不在 appMarkList 中
            boolean isNotInList = Arrays.stream(appMarkList).noneMatch(mark -> mark.equals(appMark));

            if (isNotInList) {
                // 不属于内部开发的APP，直接返回第三方跳转地址
                Integer appId = appManageList.get(i).getId();
                LambdaQueryWrapper<AppDownload> downloadWrapper =  new LambdaQueryWrapper<>();
                downloadWrapper.eq(AppDownload::getAppId, appId);
                downloadWrapper.eq(AppDownload::getAppChannel, 3);
                AppDownload appDownload = appDownloadService.getOne(downloadWrapper);
                map.put("appQRLink", appDownload == null ? "" : appDownload.getAppDownloadLink());
            }else {
                map.put("appQRLink", "https://admin.stylish-wearable.com/public/produce/app/qrcode/"+appMark);
            }
            result.add(map);
        }
        return R.ok(result);
    }

    @GetMapping("/getApp")
    public R<?> getAppManageObj(@RequestParam(value = "appName", required = false , defaultValue = "0") Integer appName) {
        Map<String, Object> result = new HashMap<>();
        AppManageVO appManageVO = new AppManageVO();


        LambdaQueryWrapper<AppManage> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AppManage::getUserAppName, appName);
        AppManage appManage = appManageService.getOne(queryWrapper);

        if (appManage == null) {
            result.put("appObj", null);
            return R.ok(result);
        }


        //包管理
        List<AppPackeageManage> appPackeageManageList = appPackeageManageService.list();


        // 使用 stream 根据 appid 分组
        Map<Integer, List<AppPackeageManageVO>> appPackageManageMap = appPackeageManageList.stream()
                .map(appPackeageManage -> {
                    AppPackeageManageVO appPackeageManageVO = new AppPackeageManageVO();
                    BeanUtil.copyProperties(appPackeageManage, appPackeageManageVO);

                    appPackeageManageVO.setVersionCode(Integer.valueOf(appPackeageManage.getVersion()));
                    appPackeageManageVO.setVersionName(appPackeageManage.getAppVersion());

                    return appPackeageManageVO;
                })
                .collect(Collectors.groupingBy(AppPackeageManageVO::getAppId));


        Map<String, Object> params = getParamsMap(appManage);

        BeanUtil.copyProperties(appManage, appManageVO);
        appManageVO.setAppPackages(appPackageManageMap.get(appManage.getId()) == null ? new ArrayList<>() : appPackageManageMap.get(appManage.getId()));
        appManageVO.setParams(params);
        result.put("appObj", appManageVO);

        return R.ok(result);
    }

    private Map<String, Object> getParamsMap(AppManage appManage) {
        Map<String, Object> params = new HashMap<>();
        String paramCollection = appManage.getParamCollection();
        if (StrUtil.isNotBlank(paramCollection)) {
            //所有参数
            List<ParamConfig> paramConfigs = paramConfigService.list();

            // 将 paramCollection 的 JSON 字符串转换为 Map
            Map<String, String> paramCollectionMap = JSONUtil.toBean(paramCollection, Map.class);

            // 创建 ID 到 paramName 的映射
            Map<String, String> paramConfigMap = paramConfigs.stream()
                    .collect(Collectors.toMap(
                            paramConfig -> paramConfig.getId().toString(),
                            ParamConfig::getParamName
                    ));

            // 遍历 paramCollectionMap，将 ID 替换为 paramName
            for (Map.Entry<String, String> entry : paramCollectionMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (paramConfigMap.containsKey(key)) {
                    // 将 ID 替换为 paramName
                    params.put(paramConfigMap.get(key), value);
                } else {
                    // 如果没有找到对应的 paramName，忽略
                }
            }

        }
        return params;
    }

    @GetMapping("/sys/getList")
    public R<?> sysGetAppManageList() {
        return R.ok(appManageService.list());
    }

    // update
    @PostMapping("/sys/update")
    public R<?> updateAppManage(@RequestBody AppManage appManage) {
        return R.ok(appManageService.updateById(appManage));
    }

    // delete
    @PostMapping("/sys/delete")
    public R<?> deleteAppManage(@RequestBody AppManage appManage) {
        appManageService.removeById(appManage.getId());
        return R.ok();
    }

    @PostMapping("/sys/save")
    public R<?> saveAppManage(@RequestBody AppManage appManage) {
        appManageService.save(appManage);
        int savedId = appManage.getId();
        return R.ok(savedId);
    }
}
