package com.kieslect.device.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.AppManage;
import com.kieslect.device.domain.AppPackeageManage;
import com.kieslect.device.domain.vo.AppManageVO;
import com.kieslect.device.domain.vo.AppPackeageManageVO;
import com.kieslect.device.service.IAppManageService;
import com.kieslect.device.service.IAppPackeageManageService;
import com.kieslect.device.service.IArticleService;
import com.kieslect.device.service.IParamConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
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

    @GetMapping("/getApp")
    public R<?> getAppManageObj(@RequestParam(value = "appMark", required = false)String appMark) {
        Map<String,Object> result = new HashMap<>();
        AppManageVO appManageVO = new AppManageVO();


        QueryWrapper<AppManage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_mark",appMark);
        AppManage appManage =   appManageService.getOne(queryWrapper);

        if (appManage == null){
            result.put("appObj", null);
            return R.ok(result);
        }


        //包管理
        List<AppPackeageManage> appPackeageManageList = appPackeageManageService.list();



        // 使用 stream 根据 appid 分组
        Map<Integer, List<AppPackeageManageVO>> appPackageManageMap = appPackeageManageList.stream()
                .map(appPackeageManage -> {
                    AppPackeageManageVO appPackeageManageVO = new AppPackeageManageVO();
                    BeanUtil.copyProperties(appPackeageManage,appPackeageManageVO);

                    appPackeageManageVO.setVersionCode(Integer.valueOf(appPackeageManage.getVersion()));
                    appPackeageManageVO.setVersionName(appPackeageManage.getAppVersion());

                    return appPackeageManageVO;
                })
                .collect(Collectors.groupingBy(AppPackeageManageVO::getAppId));


        Map<String,Object> params = new HashMap<>();
        params.put("web_agreement",7);
        params.put("web_privacy_policy",8);

        BeanUtil.copyProperties(appManage,appManageVO);
        appManageVO.setAppPackages(appPackageManageMap.get(appManage.getId()) == null ? new ArrayList<>() : appPackageManageMap.get(appManage.getId()));
        appManageVO.setParams(params);
        result.put("appObj", appManageVO);

        return R.ok(result);
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
