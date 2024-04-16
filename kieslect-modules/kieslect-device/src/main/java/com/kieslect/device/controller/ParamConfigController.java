package com.kieslect.device.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.ParamConfig;
import com.kieslect.device.domain.vo.ParamConfigVO;
import com.kieslect.device.service.IParamConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-04-01
 */
@RestController
@RequestMapping("/paramConfig")
public class ParamConfigController {

    @Autowired
    private IParamConfigService paramConfigService;

    @PostMapping("/sys/save")
    public R<?> saveParamConfig(@RequestBody ParamConfig paramConfig) {
        paramConfig.setCreateTime(String.valueOf(Instant.now().getEpochSecond()));
        paramConfig.setUpdateTime(String.valueOf(Instant.now().getEpochSecond()));
        paramConfigService.save(paramConfig);
        return R.ok();
    }
    @PostMapping("/sys/update")
    public R<?> updateParamConfig(@RequestBody ParamConfig paramConfig) {
        paramConfig.setUpdateTime(String.valueOf(Instant.now().getEpochSecond()));
        paramConfigService.updateById(paramConfig);
        return R.ok();
    }

    @PostMapping("/sys/delete")
    public R<?> deleteParamConfig(@RequestBody ParamConfig paramConfig) {
        paramConfigService.removeById(paramConfig.getId());
        return R.ok();
    }

    @PostMapping("/sys/getList")
    public R<?> getParamConfigList(@RequestBody ParamConfigVO paramConfigVO) {
        return R.ok(paramConfigService.getList(paramConfigVO));
    }
}
