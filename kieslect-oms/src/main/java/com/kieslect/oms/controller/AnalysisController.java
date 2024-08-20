package com.kieslect.oms.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.oms.service.IAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oms/analysis")
public class AnalysisController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AnalysisController.class);

    @Autowired
    private IAnalysisService analysisService;

    @GetMapping("/device/daily/getList")
    public R<?> deviceDailyGetList() {
        List<Map<String, Object>> list = analysisService.selectActivationCountByGroup();
        return R.ok(list);
    }

    @GetMapping("/country/month/getList")
    public R<?> countryMonthGetList() {
        List<Map<String, Object>> list = analysisService.selectCountryMonthCountByGroup();
        return R.ok(list);
    }
}
