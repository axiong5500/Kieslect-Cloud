package com.kieslect.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.api.model.ThirdUserInfoVO;
import com.kieslect.user.domain.ThirdUserInfo;
import com.kieslect.user.mapper.ThirdUserInfoMapper;
import com.kieslect.user.service.IThirdUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public Optional<ThirdUserInfo> findByUserIdAndThirdId(String thirdId, int thirdTokenType) {
        LambdaQueryWrapper<ThirdUserInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ThirdUserInfo::getThirdId, thirdId);
        queryWrapper.eq(ThirdUserInfo::getThirdTokenType, thirdTokenType);
        ThirdUserInfo thirdUserInfo = this.getOne(queryWrapper);
        return Optional.ofNullable(thirdUserInfo);
    }

    @Override
    public List<ThirdUserInfoVO> getThirdUserInfosByUserId(Long userId) {
        List<ThirdUserInfoVO> thirdUserInfoVOList = new ArrayList<>();
        QueryWrapper<ThirdUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<ThirdUserInfo> thirdUserInfos = thirdUserInfoMapper.selectList(queryWrapper);
        if (thirdUserInfos != null) {
            thirdUserInfoVOList = thirdUserInfos.stream().map(thirdUserInfo -> {
                ThirdUserInfoVO thirdUserInfoVO = new ThirdUserInfoVO();
                BeanUtil.copyProperties(thirdUserInfo, thirdUserInfoVO, CopyOptions.create().setFieldMapping(
                        Map.of("id", "kid")
                ));
                return thirdUserInfoVO;
            }).toList();
        }
        return thirdUserInfoVOList;
    }
}
