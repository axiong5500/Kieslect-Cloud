package com.kieslect.oms.service.impl;

import com.kieslect.oms.domain.vo.ActivationQueryVO;
import com.kieslect.oms.domain.vo.CountryDailyRequestVO;
import com.kieslect.oms.domain.vo.CountryMonthRequestVO;
import com.kieslect.oms.mapper.AnalysisMapper;
import com.kieslect.oms.service.IAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AnalysisServiceImpl implements IAnalysisService {

    @Autowired
    private AnalysisMapper analysisMapper;

    @Override
    public String selectActivationCountByGroupCount(ActivationQueryVO activationQueryVO) {
        String startDate = Optional.ofNullable(activationQueryVO)
                .map(ActivationQueryVO::getStartDate)
                .orElse(null);
        String endDate = Optional.ofNullable(activationQueryVO)
                .map(ActivationQueryVO::getEndDate)
                .orElse(null);
        String countryCode = Optional.ofNullable(activationQueryVO)
                .map(ActivationQueryVO::getCountryCode)
                .orElse(null);
        String noIncludeCountryCode = Optional.ofNullable(activationQueryVO)
                .map(ActivationQueryVO::getNoIncludeCountryCode)
                .orElse(null);
        return analysisMapper.selectActivationCountByGroupCount(startDate,endDate,countryCode,noIncludeCountryCode);
    }

    @Override
    public List<Map<String, Object>> selectActivationCountByGroup(ActivationQueryVO activationQueryVO) {
        String startDate = Optional.ofNullable(activationQueryVO)
                .map(ActivationQueryVO::getStartDate)
                .orElse(null);
        String endDate = Optional.ofNullable(activationQueryVO)
                .map(ActivationQueryVO::getEndDate)
                .orElse(null);
        String countryCode = Optional.ofNullable(activationQueryVO)
                .map(ActivationQueryVO::getCountryCode)
                .orElse(null);
        String noIncludeCountryCode = Optional.ofNullable(activationQueryVO)
                .map(ActivationQueryVO::getNoIncludeCountryCode)
                .orElse(null);
        return analysisMapper.selectActivationCountByGroup(startDate,endDate,countryCode,noIncludeCountryCode);
    }

    @Override
    public List<Map<String, Object>> selectCountryDailyByGroup(CountryDailyRequestVO countryDailyRequestVO) {
        String startDate = Optional.ofNullable(countryDailyRequestVO)
                .map(ActivationQueryVO::getStartDate)
                .orElse(null);
        String endDate = Optional.ofNullable(countryDailyRequestVO)
                .map(ActivationQueryVO::getEndDate)
                .orElse(null);
        String countryCode = Optional.ofNullable(countryDailyRequestVO)
                .map(ActivationQueryVO::getCountryCode)
                .orElse(null);
        String noIncludeCountryCode = Optional.ofNullable(countryDailyRequestVO)
                .map(ActivationQueryVO::getNoIncludeCountryCode)
                .orElse(null);
        String category = Optional.ofNullable(countryDailyRequestVO)
                .map(CountryDailyRequestVO::getCategory)
                .orElse(null);
        return analysisMapper.selectCountryDailyByGroup(startDate,endDate,countryCode,noIncludeCountryCode,category);
    }

    @Override
    public List<Map<String, Object>> selectCountryMonthCountByGroup(CountryMonthRequestVO countryMonthRequestVO) {
        String currentYear = Optional.ofNullable(countryMonthRequestVO)
                .map(CountryMonthRequestVO::getCurrentYear)
                .orElse(null);
        String countryCode = Optional.ofNullable(countryMonthRequestVO)
                .map(CountryMonthRequestVO::getCountryCode)
                .orElse(null);
        String category = Optional.ofNullable(countryMonthRequestVO)
                .map(CountryMonthRequestVO::getCategory)
                .orElse(null);
        return analysisMapper.selectCountryMonthCountByGroup(currentYear, countryCode, category);
    }


    @Override
    public List<Map<String, Object>> getMonthlyActivationCount() {
        return analysisMapper.selectMonthlyActivationCount();
    }


}
