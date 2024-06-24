package com.kieslect.outapi.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.outapi.annotation.ApiOpenTime;
import com.kieslect.outapi.domain.ThirdDeviceActiveInfo;
import com.kieslect.outapi.service.IThirdDeviceActiveInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-06-24
 */
@RestController
@RequestMapping("/thirdDeviceActiveInfo")
@RefreshScope
public class ThirdDeviceActiveInfoController {

    private static final Logger logger = LoggerFactory.getLogger(ThirdDeviceActiveInfoController.class);

    @Autowired
    private IThirdDeviceActiveInfoService thirdDeviceActiveInfoService;


    @Value("${max.records:1000}")
    private int MAX_RECORDS;

    @Transactional(rollbackFor = Exception.class)
    @ApiOpenTime // 添加时间开放控制注解
    @PostMapping("/batchInsert")
    public R<?> bulkInsert(@RequestBody List<ThirdDeviceActiveInfo> records) {
        if (records.size() > MAX_RECORDS) {
            return R.fail(HttpStatus.BAD_REQUEST,"Batch size exceeds the maximum limit of " + MAX_RECORDS);
        }
        boolean result = thirdDeviceActiveInfoService.saveBatch(records);
        return R.ok(result, "Batch insert operation completed");
    }
}
