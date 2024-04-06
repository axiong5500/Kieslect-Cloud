package com.kieslect.device.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kieslect.device.domain.ParamConfig;
import com.kieslect.device.domain.vo.ParamConfigVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kieslect
 * @since 2024-04-01
 */
@Mapper
public interface ParamConfigMapper extends BaseMapper<ParamConfig> {

    List<ParamConfig> getList(ParamConfigVO paramConfigVO);
}
