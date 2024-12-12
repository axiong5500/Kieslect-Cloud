package com.kieslect.file.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.file.domain.SportKlocation;
import com.kieslect.file.domain.vo.SportKlocationVO;
import com.kieslect.file.mapper.SportKlocationMapper;
import com.kieslect.file.service.ISportKlocationService;
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
public class SportKlocationServiceImpl extends ServiceImpl<SportKlocationMapper, SportKlocation> implements ISportKlocationService {

    @Autowired
    private SportKlocationMapper sportKlocationMapper;
    @Override
    public List<SportKlocationVO> listByUserId(Long userId) {
        return sportKlocationMapper.listByUserId(userId);
    }
}
