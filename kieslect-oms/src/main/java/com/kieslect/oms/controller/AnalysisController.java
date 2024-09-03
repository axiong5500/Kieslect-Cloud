package com.kieslect.oms.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.oms.domain.vo.ActivationQueryVO;
import com.kieslect.oms.domain.vo.CountryMonthRequestVO;
import com.kieslect.oms.service.IAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/oms/analysis")
public class AnalysisController {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(AnalysisController.class);

    @Autowired
    private IAnalysisService analysisService;

    @PostMapping("/device/daily/getList")
    public R<?> deviceDailyGetList(@RequestBody(required = false) ActivationQueryVO activationQueryVO) {
        List<Map<String, Object>> list = analysisService.selectActivationCountByGroup(activationQueryVO);
        return R.ok(list);
    }

    @PostMapping("/device/daily/getList/count")
    public R<?> deviceDailyGetListCount(@RequestBody(required = false) ActivationQueryVO activationQueryVO) {
        String count = analysisService.selectActivationCountByGroupCount(activationQueryVO);
        return R.ok(count);
    }

    @PostMapping("/country/month/getList")
    public R<?> countryMonthGetList(@RequestBody(required = false) CountryMonthRequestVO requestVO) {
        List<Map<String, Object>> list = analysisService.selectCountryMonthCountByGroup(requestVO);
        return R.ok(list);
    }
}
