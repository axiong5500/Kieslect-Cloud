package com.kieslect.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.common.core.domain.R;
import com.kieslect.device.domain.Issue;
import com.kieslect.device.service.IIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    private IIssueService issueService;


    @PostMapping("/sys/getList")
    public R<?> sysGetIssueManageList(@RequestBody Issue issue) {
        LambdaQueryWrapper<Issue> queryWrapper = new LambdaQueryWrapper<>();
        if (issue.getIssueStatus() != null) {
            queryWrapper.eq(Issue::getIssueStatus, issue.getIssueStatus());
        }
        if (issue.getAppName() != null) {
            queryWrapper.eq(Issue::getAppName, issue.getAppName());
        }
        if (issue.getAppSystem() != null) {
            queryWrapper.eq(Issue::getAppSystem, issue.getAppSystem());
        }
        queryWrapper.orderByDesc(Issue::getCreateTime); // 按照创建时间升序
        return R.ok(issueService.list(queryWrapper));
    }


    @PostMapping("/sys/update")
    public R<?> updateIssue(@RequestBody Issue issue) {
        issue.setUpdateTime(Instant.now().getEpochSecond());
        LambdaQueryWrapper<Issue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Issue::getId, issue.getId());
        return R.ok(issueService.update(issue,lambdaQueryWrapper));
    }


    @PostMapping("/sys/delete")
    public R<?> deleteIssue(@RequestBody Issue issue) {
        LambdaQueryWrapper<Issue> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Issue::getId, issue.getId());
        return R.ok(issueService.remove(lambdaQueryWrapper));
    }


    @PostMapping("/sys/save")
    public R<?> saveIssue(@RequestBody Issue issue) {
        issue.setCreateTime(Instant.now().getEpochSecond());
        issue.setUpdateTime(Instant.now().getEpochSecond());
        return R.ok(issueService.save(issue));
    }

}
