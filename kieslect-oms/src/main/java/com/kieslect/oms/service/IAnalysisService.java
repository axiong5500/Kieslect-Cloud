package com.kieslect.oms.service;

import java.util.List;
import java.util.Map;

public interface IAnalysisService {
    List<Map<String, Object>> selectActivationCountByGroup();

    List<Map<String, Object>> selectCountryMonthCountByGroup();
}
