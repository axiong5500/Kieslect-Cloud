package com.kieslect.oms.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.oms.domain.vo.ActivationQueryVO;
import com.kieslect.oms.domain.vo.CountryDailyRequestVO;
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

    /**
     * 日度统计
     * @param activationQueryVO
     * @return
     */
    @PostMapping("/device/daily/getList")
    public R<?> deviceDailyGetList(@RequestBody(required = false) ActivationQueryVO activationQueryVO) {
        List<Map<String, Object>> list = analysisService.selectActivationCountByGroup(activationQueryVO);
        return R.ok(list);
    }

    /**
     * 日度统计总数
     * @param activationQueryVO
     * @return
     */
    @PostMapping("/device/daily/getList/count")
    public R<?> deviceDailyGetListCount(@RequestBody(required = false) ActivationQueryVO activationQueryVO) {
        String count = analysisService.selectActivationCountByGroupCount(activationQueryVO);
        return R.ok(count);
    }

    /**
     * 日度国家统计
     * @param activationQueryVO
     * @return
     */
    @PostMapping("/country/daily/getList")
    public R<?> countryDailyGetList(@RequestBody(required = false) CountryDailyRequestVO activationQueryVO) {
        List<Map<String, Object>> list = analysisService.selectCountryDailyByGroup(activationQueryVO);
        return R.ok(list);
    }

    /**
     * 月度国家统计
     * @param requestVO
     * @return
     */
    @PostMapping("/country/month/getList")
    public R<?> countryMonthGetList(@RequestBody(required = false) CountryMonthRequestVO requestVO) {
        List<Map<String, Object>> list = analysisService.selectCountryMonthCountByGroup(requestVO);
        return R.ok(list);
    }


    /**
     * 月度激活统计-柱状图
     * @return
     */
    @GetMapping("/device/month/getList")
    public R<?> getMonthlyActivationData() {
        List<Map<String, Object>> list = analysisService.getMonthlyActivationCount();
        return R.ok(list);
    }
}
