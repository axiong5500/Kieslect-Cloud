package com.kieslect.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.file.domain.SportDetail;
import com.kieslect.file.domain.ThirdUserInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-11-22
 */
public interface ISportDetailService extends IService<SportDetail> {

    ThirdUserInfo getThirdInfoByUserId(Long userId);

    void insertOrUpdateSportDetailBatch(List<SportDetail> detailSportList);
}
