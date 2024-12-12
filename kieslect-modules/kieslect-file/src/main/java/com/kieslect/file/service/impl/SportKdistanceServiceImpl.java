package com.kieslect.file.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.file.domain.SportKdistance;
import com.kieslect.file.domain.vo.SportKdistanceVO;
import com.kieslect.file.mapper.SportKdistanceMapper;
import com.kieslect.file.service.ISportKdistanceService;
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
public class SportKdistanceServiceImpl extends ServiceImpl<SportKdistanceMapper, SportKdistance> implements ISportKdistanceService {

    @Autowired
    private SportKdistanceMapper sportKdistanceMapper;
    @Override
    public List<SportKdistanceVO> listByUserId(Long userId) {
        return sportKdistanceMapper.listByUserId(userId);
    }
}
