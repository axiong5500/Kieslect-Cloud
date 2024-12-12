package com.kieslect.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kieslect.file.domain.SportDetail;
import com.kieslect.file.domain.ThirdUserInfo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kieslect
 * @since 2024-11-22
 */
public interface SportDetailMapper extends BaseMapper<SportDetail> {

    ThirdUserInfo getThirdInfoByUserId(Long userId);

    void insertOrUpdateSportDetailBatch(List<SportDetail> detailSportList);
}
