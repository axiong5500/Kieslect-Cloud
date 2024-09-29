package com.kieslect.oms.service;

import com.kieslect.oms.domain.vo.ActivationQueryVO;
import com.kieslect.oms.domain.vo.CountryMonthRequestVO;

import java.util.List;
import java.util.Map;

public interface IAnalysisService {
    List<Map<String, Object>> selectActivationCountByGroup(ActivationQueryVO activationQueryVO);
    String selectActivationCountByGroupCount(ActivationQueryVO activationQueryVO);

    List<Map<String, Object>> selectCountryMonthCountByGroup(CountryMonthRequestVO requestVO);


    List<Map<String, Object>> getMonthlyActivationCount();
}
