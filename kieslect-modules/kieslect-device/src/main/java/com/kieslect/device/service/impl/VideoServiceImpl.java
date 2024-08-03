package com.kieslect.device.service.impl;

import com.kieslect.device.domain.Video;
import com.kieslect.device.mapper.VideoMapper;
import com.kieslect.device.service.IVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-07-27
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {

}
