package com.kieslect.device.service.impl;

import com.kieslect.device.domain.PolicyInfo;
import com.kieslect.device.mapper.PolicyInfoMapper;
import com.kieslect.device.service.IPolicyInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 策略信息 服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-11-14
 */
@Service
public class PolicyInfoServiceImpl extends ServiceImpl<PolicyInfoMapper, PolicyInfo> implements IPolicyInfoService {

}
