package com.kieslect.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.Icon;
import com.kieslect.device.service.IIconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-07-04
 */
@RestController
@RequestMapping("/icon")
public class IconController {
    @Autowired
    IIconService iconService;

    @GetMapping("/sys/getList")
    public R<?> sysGetIconManageList() {
        return R.ok(iconService.list());
    }


    @PostMapping("/sys/update")
    public R<?> updateIcon(@RequestBody Icon icon) {
        icon.setUpdateTime(LocalDateTime.now());
        return R.ok(iconService.updateById(icon));
    }


    @PostMapping("/sys/delete")
    public R<?> deleteIcon(@RequestBody Icon icon) {
        LambdaQueryWrapper<Icon> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Icon::getId, icon.getId());
        return R.ok(iconService.remove(lambdaQueryWrapper));
    }


    @PostMapping("/sys/save")
    public R<?> saveIcon(@RequestBody Icon icon) {
        icon.setCreateTime(LocalDateTime.now());
        icon.setUpdateTime(LocalDateTime.now());
        return R.ok(iconService.save(icon));
    }
}
