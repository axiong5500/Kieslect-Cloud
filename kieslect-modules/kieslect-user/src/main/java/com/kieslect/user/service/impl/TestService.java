package com.kieslect.user.service.impl;

import com.kieslect.common.redis.service.RedisService;
import com.kieslect.user.mapper.TestMapper;
import com.kieslect.user.service.ITestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService implements ITestService {

    @Autowired
    private TestMapper testMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public int deleteConfigById(Long configId) {
        redisService.deleteObject("token");
        return testMapper.deleteConfigById(configId);
    }
}
