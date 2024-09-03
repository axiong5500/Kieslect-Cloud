package com.kieslect.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kieslect.oms.domain.Analysis;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AnalysisMapper extends BaseMapper<Analysis> {

    /**
     * 查询每日激活量
     * @param startDate
     * @param endDate
     * @param country
     * @return
     */
    @MapKey("activationDate")
    List<Map<String, Object>> selectActivationCountByGroup(@Param("startDate") String startDate,
                                                           @Param("endDate") String endDate,
                                                           @Param("country") String country);

    /**
     * 查询每日激活量总数量
     * @param startDate
     * @param endDate
     * @param country
     * @return
     */
    String selectActivationCountByGroupCount(@Param("startDate") String startDate,
                                                           @Param("endDate") String endDate,
                                                           @Param("country") String country);


    /**
     * 查询每月激活量
     * @param year
     * @param country
     * @param category
     * @return
     */
    @MapKey("totalCount")
    List<Map<String, Object>> selectCountryMonthCountByGroup(@Param("year") String year,
                                                             @Param("country") String country,
                                                             @Param("category") String category);
}
