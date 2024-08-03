package com.kieslect.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.Video;
import com.kieslect.device.service.IVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-07-27
 */
@RestController
@RequestMapping("/video")
public class VideoController {
    @Autowired
    private IVideoService videoService;


    @GetMapping("/sys/getList")
    public R<?> sysGetVideoManageList() {
        LambdaQueryWrapper<Video> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(Video::getDelStatus, 1); // 删除状态为1的
        queryWrapper.orderByDesc(Video::getCreateTime); // 按照创建时间升序
        return R.ok(videoService.list(queryWrapper));
    }


    @PostMapping("/sys/update")
    public R<?> updateVideo(@RequestBody Video video) {
        video.setUpdateTime(Instant.now().getEpochSecond());
        LambdaQueryWrapper<Video> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Video::getId, video.getId());
        return R.ok(videoService.update(video,lambdaQueryWrapper));
    }


    @PostMapping("/sys/delete")
    public R<?> deleteVideo(@RequestBody Video video) {
        video.setUpdateTime(Instant.now().getEpochSecond());
        video.setDelStatus(1);
        LambdaQueryWrapper<Video> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Video::getId, video.getId());
        return R.ok(videoService.update(video,lambdaQueryWrapper));
    }


    @PostMapping("/sys/save")
    public R<?> saveVideo(@RequestBody Video video) {
        video.setCreateTime(Instant.now().getEpochSecond());
        video.setUpdateTime(Instant.now().getEpochSecond());
        video.setDelStatus(0);
        return R.ok(videoService.save(video));
    }
}
