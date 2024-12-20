package com.kieslect.user.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.redis.service.RedisService;
import com.kieslect.user.domain.IssueDetail;
import com.kieslect.user.domain.vo.IssueDetailCreateVO;
import com.kieslect.user.domain.vo.IssueDetailListVO;
import com.kieslect.user.domain.vo.IssueDetailReplyVO;
import com.kieslect.user.service.IIssueDetailService;
import com.kieslect.user.service.IIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kieslect
 * @since 2024-11-25
 */
@RestController
@RequestMapping("/issueDetail")
public class IssueDetailController {

    @Autowired
    IIssueDetailService issueDetailService;
    @Autowired
    RedisService redisService;
    @Autowired
    IIssueService issueService;
    private static final String ISSUE_READ_STATUS_PREFIX = "issue_read_status:";

    private static String getissueReadStatusUserRedisKey(String redisKey) {
        return ISSUE_READ_STATUS_PREFIX + redisKey;
    }

    @PostMapping("/create")
    public R<?> createIssueDetail(@RequestBody IssueDetailCreateVO issueDetailVO) {
        int result = issueDetailService.createIssueDetail(issueDetailVO);
        // 更新问题与反馈的更新时间,更新问题状态
        issueService.update().set("update_time", Instant.now().getEpochSecond()).set("issue_status", 0).eq("id", issueDetailVO.getIssueId()).update();
        return R.ok(result);
    }

    /**
     * 回复issue详情
     *
     * @param issueDetailReplyVO
     * @return
     */
    @PostMapping("/sys/reply")
    public R<?> replyIssueDetail(@RequestBody IssueDetailReplyVO issueDetailReplyVO) {
        // 回复消息
        int result = issueDetailService.replyIssueDetail(issueDetailReplyVO);
        // 更新问题与反馈的更新时间、更新问题状态
        Integer issueStatus = issueDetailReplyVO.getIssueStatus();
        if (issueStatus != null && (issueStatus == 1 || issueStatus == 2)){
            issueService.update().set("update_time", Instant.now().getEpochSecond()).set("issue_status", issueStatus).eq("id", issueDetailReplyVO.getIssueId()).update();
        }else{
            issueService.update().set("update_time", Instant.now().getEpochSecond()).eq("id", issueDetailReplyVO.getIssueId()).update();
        }
        // 再删除缓存键
        String issueReadRedisKey = getissueReadStatusUserRedisKey(issueDetailReplyVO.getIssueId().toString());
        if (redisService.hasKey(issueReadRedisKey)) {
            redisService.deleteObject(issueReadRedisKey);
        }
        return R.ok(result);
    }

    @GetMapping("/sys/getIssueDetail")
    public R<?> getSysIssueDetail(@RequestParam(value = "issueId") Long issueId) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<IssueDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IssueDetail::getIssueId, issueId);
        queryWrapper.orderByAsc(IssueDetail::getCreateTime);
        List<IssueDetail> issueList = issueDetailService.list(queryWrapper);
        List<IssueDetailListVO> issueResultList = issueList.stream().map(issue -> {
            IssueDetailListVO issueListVO = new IssueDetailListVO();
            issueListVO.setIssueId(issue.getId());
            issueListVO.setIssueUserId(issue.getIssueUserId());
            issueListVO.setIssueMsg(issue.getIssueMsg());
            issueListVO.setFilePath(issue.getFilePath());
            issueListVO.setIssueFileType(issue.getIssueFileType());
            issueListVO.setCreateTime(issue.getCreateTime());
            return issueListVO;
        }).collect(Collectors.toList());
        result.put("issueDetailList", issueResultList);
        return R.ok(result);
    }

    @GetMapping("/getIssueDetail")
    public R<?> getIssueDetail(@RequestParam(value = "issueId") Long issueId) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<IssueDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(IssueDetail::getIssueId, issueId);
        queryWrapper.orderByAsc(IssueDetail::getCreateTime);
        List<IssueDetail> issueList = issueDetailService.list(queryWrapper);
        List<IssueDetailListVO> issueResultList = issueList.stream().map(issue -> {
            IssueDetailListVO issueListVO = new IssueDetailListVO();
            issueListVO.setIssueId(issue.getId());
            issueListVO.setIssueUserId(issue.getIssueUserId());
            issueListVO.setIssueMsg(issue.getIssueMsg());
            issueListVO.setFilePath(issue.getFilePath());
            issueListVO.setIssueFileType(issue.getIssueFileType());
            issueListVO.setCreateTime(issue.getCreateTime());
            return issueListVO;
        }).collect(Collectors.toList());
        result.put("issueDetailList", issueResultList);
        // 更新外层列表红点已读状态
        redisService.setCacheObject(getissueReadStatusUserRedisKey(issueId.toString()), 1);
        return R.ok(result);
    }
}
