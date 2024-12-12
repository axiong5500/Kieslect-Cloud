package com.kieslect.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.file.domain.SportDetail;
import com.kieslect.file.domain.ThirdUserInfo;
import com.kieslect.file.mapper.SportDetailMapper;
import com.kieslect.file.service.ISportDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-11-22
 */
@Service
public class SportDetailServiceImpl extends ServiceImpl<SportDetailMapper, SportDetail> implements ISportDetailService {

    @Autowired
    private SportDetailMapper sportDetailMapper;
    @Override
    public ThirdUserInfo getThirdInfoByUserId(Long userId) {
        return sportDetailMapper.getThirdInfoByUserId(userId);
    }

    @Override
    public void insertOrUpdateSportDetailBatch(List<SportDetail> detailSportList) {
        sportDetailMapper.insertOrUpdateSportDetailBatch(detailSportList);
    }
}
