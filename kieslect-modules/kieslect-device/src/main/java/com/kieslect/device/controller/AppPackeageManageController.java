package com.kieslect.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.AppPackeageManage;
import com.kieslect.device.domain.vo.AppPackeageManageSysVO;
import com.kieslect.device.service.IAppPackeageManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-05-22
 */
@RestController
@RequestMapping("/appPackageManage")
public class AppPackeageManageController {
    @Autowired
    IAppPackeageManageService appPackeageManageService;
    @PostMapping("/sys/getList")
    public R<?> sysGetAppPackeageManageList(@RequestBody AppPackeageManageSysVO appPackeageManageVO) {
        LambdaQueryWrapper<AppPackeageManage> query = new LambdaQueryWrapper<>();
        if (appPackeageManageVO.getAppId() != null){
            query.eq(AppPackeageManage::getAppId,appPackeageManageVO.getAppId());
        }
        if (appPackeageManageVO.getChannel() != null){
            query.eq(AppPackeageManage::getChannel,appPackeageManageVO.getChannel());
        }
        return R.ok(appPackeageManageService.list(query));
    }
    // update
    @PostMapping("/sys/update")
    public R<?> updateAppPackeageManage(@RequestBody AppPackeageManage appPackeageManage) {
        return R.ok(appPackeageManageService.updateById(appPackeageManage));
    }
    // delete
    @PostMapping("/sys/delete")
    public R<?> deleteAppPackeageManage(@RequestBody AppPackeageManage appPackeageManage) {
        appPackeageManageService.removeById(appPackeageManage.getId());
        return R.ok();
    }

    @PostMapping("/sys/save")
    public R<?> saveAppPackeageManage(@RequestBody AppPackeageManage appPackeageManage) {
        appPackeageManageService.save(appPackeageManage);
        int savedId = appPackeageManage.getId();
        return R.ok(savedId);
    }
}
