package com.kieslect.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.user.domain.ThirdUserInfo;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-06-14
 */
public interface IThirdUserInfoService extends IService<ThirdUserInfo> {

    Optional<ThirdUserInfo> findByUserIdAndThirdId(Long userId, String thirdId);

    List<ThirdUserInfo> getThirdUserInfo(Long userId);
}
