package com.kieslect.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.redis.service.RedisService;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.user.domain.ImageManage;
import com.kieslect.user.domain.Issue;
import com.kieslect.user.domain.MsgLevel;
import com.kieslect.user.domain.vo.IssueListVO;
import com.kieslect.user.domain.vo.IssueVO;
import com.kieslect.user.service.IImageManageService;
import com.kieslect.user.service.IIssueService;
import com.kieslect.user.service.IMsgLevelService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    private IIssueService issueService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private IMsgLevelService msgLevelService;
    @Autowired
    private IImageManageService imageManageService;
    @Autowired
    private RedisService redisService;
    private static final String IMAGE_PREFIX = "image_read_status:";
    private static final String ISSUE_READ_STATUS_PREFIX = "issue_read_status:";
    private static String getissueReadStatusUserRedisKey(String redisKey) {
        return ISSUE_READ_STATUS_PREFIX + redisKey;
    }
    private static String getImageReadingStatusUserRedisKey(String redisKey) {
        return IMAGE_PREFIX + redisKey;
    }

    /**
     * 问题与反馈
     *
     * @param request
     * @param issueVO
     * @return
     */
    @PostMapping("/issues_suggestions")
    public R<?> createIssue(HttpServletRequest request, @RequestBody IssueVO issueVO) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        issueVO.setUserId(loginUser.getKid());
        issueService.createIssue(issueVO);
        return R.ok();
    }


    /**
     * 消息中心，获取问题列表
     *
     * @param request
     * @return
     */
    @GetMapping("/getList")
    public R<?> getList(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getKid();
        Byte appName = loginUser.getAppName();
        // 获取消息等级
        LambdaQueryWrapper<MsgLevel> msgLevelQueryWrapper =  new LambdaQueryWrapper<>();
        msgLevelQueryWrapper.eq(MsgLevel::getMsgType, 1);
        msgLevelQueryWrapper.eq(MsgLevel::getAppName, appName);
        MsgLevel msgLevel = msgLevelService.getOne(msgLevelQueryWrapper);
        int issueLevel = msgLevel.getMsgLevel();


        //获取问题与反馈消息
        LambdaQueryWrapper<Issue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Issue::getUserId, userId);
        queryWrapper.orderByDesc(Issue::getUpdateTime);
        List<Issue> issueList = issueService.list(queryWrapper);
        List<IssueListVO> issueResultList = issueList.stream().map(issue -> {
            boolean readStatus = redisService.hasKey(getissueReadStatusUserRedisKey(issue.getId().toString()));
            IssueListVO issueListVO = new IssueListVO();
            issueListVO.setIssueId(issue.getId());
            issueListVO.setDescription(issue.getDescription());
            issueListVO.setCreateTime(issue.getUpdateTime());
            issueListVO.setReadStatus(readStatus);
            issueListVO.setIssueLevel(issueLevel);
            issueListVO.setMsgType(1);
            return issueListVO;
        }).collect(Collectors.toList());

        // 获取广告位地址
        LambdaQueryWrapper<ImageManage> imageQueryWrapper = new LambdaQueryWrapper<>();
        imageQueryWrapper.eq(ImageManage::getImageType, 1);
        imageQueryWrapper.eq(ImageManage::getAppName, appName);
        imageQueryWrapper.eq(ImageManage::getImageStatus, 1);
        List<ImageManage> imageManageList = imageManageService.list(imageQueryWrapper);
        List<IssueListVO> imageResultList = imageManageList.stream().map(image -> {
            boolean readStatus = redisService.hasKey(getImageReadingStatusUserRedisKey(image.getId() + "_" + userId));

            IssueListVO issueListVO = new IssueListVO();
            issueListVO.setIssueId(Long.valueOf(image.getId()));
            issueListVO.setRedirectUrl(image.getRedirectUrl());
            issueListVO.setDescription(image.getDescription());
            issueListVO.setCreateTime(image.getCreateTime());
            issueListVO.setReadStatus(readStatus);
            issueListVO.setIssueLevel(issueLevel);
            issueListVO.setImageUrl(image.getImageUrl());
            issueListVO.setMsgType(2);
            return issueListVO;
        }).collect(Collectors.toList());

        List<IssueListVO> mergedList = new ArrayList<>();
        mergedList.addAll(issueResultList);
        mergedList.addAll(imageResultList);

        // 使用原地排序提高性能
        Collections.sort(mergedList, Comparator.comparing(IssueListVO::getCreateTime).reversed());

        result.put("issueList", mergedList);
        return R.ok(result);
    }

    /**
     * 已读全部
     *
     * @param request
     * @return
     */
    @GetMapping("/readAll")
    public R<?> readAll(HttpServletRequest request) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getKid();
        Byte appName = loginUser.getAppName();
        // 先更新问题与反馈
        LambdaUpdateWrapper<Issue> queryWrapper = new LambdaUpdateWrapper<>();
        queryWrapper.eq(Issue::getUserId, userId);
        List<Issue> issueList = issueService.list(queryWrapper);
        for (Issue issue : issueList) {
            redisService.setCacheObject(getissueReadStatusUserRedisKey(issue.getId().toString()), 1);
        }
        // 再更新广告位
        LambdaQueryWrapper<ImageManage> imageQueryWrapper = new LambdaQueryWrapper<>();
        imageQueryWrapper.eq(ImageManage::getImageType, 1);
        imageQueryWrapper.eq(ImageManage::getAppName, appName);
        imageQueryWrapper.eq(ImageManage::getImageStatus, 1);
        List<ImageManage> imageManageList = imageManageService.list(imageQueryWrapper);
        for (ImageManage imageManage : imageManageList) {
            redisService.setCacheObject(getImageReadingStatusUserRedisKey(imageManage.getId() + "_" + userId), 1);
        }
        return R.ok(1);
    }

    /**
     *
     *  点击广告位跳转更新红点状态
     * @param request
     * @return
     */
    @GetMapping("/markImageRead")
    public R<?> markImageRead(HttpServletRequest request,@RequestParam("issueId") Long imageId) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        Long userId = loginUser.getKid();
        redisService.setCacheObject(getImageReadingStatusUserRedisKey(imageId + "_" + userId), 1);
        return R.ok(1);
    }


}
