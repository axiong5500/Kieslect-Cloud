package com.kieslect.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.file.domain.SportKdistance;
import com.kieslect.file.domain.vo.SportKdistanceVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-11-22
 */
public interface ISportKdistanceService extends IService<SportKdistance> {

    List<SportKdistanceVO> listByUserId(Long userId);
}
