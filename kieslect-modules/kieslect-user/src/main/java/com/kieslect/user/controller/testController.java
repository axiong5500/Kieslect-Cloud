package com.kieslect.user.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.user.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class testController {
    @Autowired
    private ITestService testService;

    /**
     * 根据配置ID删除配置信息
     *
     * @param configId 配置的ID，用于指定要删除的配置项
     * @return 返回操作结果，成功删除返回true，失败返回false
     */
    @PostMapping("/deleteConfigById")
    public R<?> deleteConfigById(@RequestBody Long configId){
        // 调用测试服务，根据配置ID删除配置
        int result = testService.deleteConfigById(configId);
        // 返回操作结果
        return R.ok(result);
    }

}
