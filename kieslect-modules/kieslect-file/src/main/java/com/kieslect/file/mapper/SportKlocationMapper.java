package com.kieslect.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kieslect.file.domain.SportKlocation;
import com.kieslect.file.domain.vo.SportKlocationVO;
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
public interface SportKlocationMapper extends BaseMapper<SportKlocation> {

    List<SportKlocationVO> listByUserId(@Param("userId") Long userId);
}
