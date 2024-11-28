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
     *
     * @param startDate
     * @param endDate
     * @param country
     * @param noIncludeCountryCode
     * @return
     */
    @MapKey("activationDate")
    List<Map<String, Object>> selectActivationCountByGroup(@Param("startDate") String startDate,
                                                           @Param("endDate") String endDate,
                                                           @Param("country") String country,@Param("noIncludeCountryCode") String noIncludeCountryCode);

    /**
     * 查询每日激活量总数量
     *
     * @param startDate
     * @param endDate
     * @param country
     * @param noIncludeCountryCode
     * @return
     */
    String selectActivationCountByGroupCount(@Param("startDate") String startDate,
                                             @Param("endDate") String endDate,
                                             @Param("country") String country,@Param("noIncludeCountryCode") String noIncludeCountryCode);


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


    /**
     * 查询每月激活量
     * @return
     */
    @MapKey("month")
    List<Map<String, Object>> selectMonthlyActivationCount();

    /**
     * 查询每日激活量
     *
     * @param startDate
     * @param endDate
     * @param country
     * @param noIncludeCountryCode
     * @param category
     * @return
     */
    @MapKey("activationDate")
    List<Map<String, Object>> selectCountryDailyByGroup(@Param("startDate") String startDate,
                                                        @Param("endDate") String endDate,
                                                        @Param("country") String country,
                                                        @Param("noIncludeCountryCode") String noIncludeCountryCode,
                                                        @Param("category")String category);
}
