package com.kieslect.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kieslect.file.domain.SportKdistance;
import com.kieslect.file.domain.vo.SportKdistanceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kieslect
 * @since 2024-11-22
 */
public interface SportKdistanceMapper extends BaseMapper<SportKdistance> {

    List<SportKdistanceVO> listByUserId(@Param("userId") Long userId);
}
