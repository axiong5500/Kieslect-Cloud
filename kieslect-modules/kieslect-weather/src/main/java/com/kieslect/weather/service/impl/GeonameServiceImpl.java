package com.kieslect.weather.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.weather.domain.Geoname;
import com.kieslect.weather.mapper.GeonameMapper;
import com.kieslect.weather.service.IGeonameService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-05-30
 */
@Service
public class GeonameServiceImpl extends ServiceImpl<GeonameMapper, Geoname> implements IGeonameService {

}
