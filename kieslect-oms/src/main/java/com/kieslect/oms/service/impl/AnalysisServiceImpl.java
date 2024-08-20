package com.kieslect.oms.service.impl;

import com.kieslect.oms.mapper.AnalysisMapper;
import com.kieslect.oms.service.IAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AnalysisServiceImpl implements IAnalysisService {

    @Autowired
    private AnalysisMapper analysisMapper;

    public List<Map<String, Object>> selectActivationCountByGroup() {
        return analysisMapper.selectActivationCountByGroup();
    }

    @Override
    public List<Map<String, Object>> selectCountryMonthCountByGroup() {
        return analysisMapper.selectCountryMonthCountByGroup();
    }
}
