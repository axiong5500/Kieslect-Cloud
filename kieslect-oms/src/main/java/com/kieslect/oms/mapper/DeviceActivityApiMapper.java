package com.kieslect.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kieslect.oms.domain.Analysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceActivityApiMapper extends BaseMapper<Analysis> {

    List<Map<String, Object>> getList(@Param("startDate") Long startDate,@Param("endDate") Long endDate);
}
