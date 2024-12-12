package com.kieslect.file.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.file.domain.SportKlocation;
import com.kieslect.file.domain.vo.SportKlocationVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-11-22
 */
public interface ISportKlocationService extends IService<SportKlocation> {

    List<SportKlocationVO> listByUserId(Long userId);
}
