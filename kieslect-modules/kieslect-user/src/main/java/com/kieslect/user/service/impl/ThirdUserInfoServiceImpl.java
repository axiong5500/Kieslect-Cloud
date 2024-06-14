package com.kieslect.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.user.domain.ThirdUserInfo;
import com.kieslect.user.mapper.ThirdUserInfoMapper;
import com.kieslect.user.service.IThirdUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-06-14
 */
@Service
public class ThirdUserInfoServiceImpl extends ServiceImpl<ThirdUserInfoMapper, ThirdUserInfo> implements IThirdUserInfoService {

    @Autowired
    private ThirdUserInfoMapper thirdUserInfoMapper;

    @Override
    public Optional<ThirdUserInfo> findByUserIdAndThirdId(Long userId, String thirdId) {
        LambdaQueryWrapper<ThirdUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ThirdUserInfo::getUserId, userId);
        queryWrapper.eq(ThirdUserInfo::getThirdId, thirdId);
        ThirdUserInfo thirdUserInfo = this.getOne(queryWrapper);
        return Optional.ofNullable(thirdUserInfo);
    }

    @Override
    public List<ThirdUserInfo> getThirdUserInfo(Long userId) {
        QueryWrapper<ThirdUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return thirdUserInfoMapper.selectList(queryWrapper);
    }
}
