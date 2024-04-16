package com.kieslect.user.controller;

import com.kieslect.common.core.domain.R;
import com.kieslect.user.domain.vo.IssueVO;
import com.kieslect.user.service.IIssueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issue")
public class IssueController {

    @Autowired
    private IIssueService issueService;
    @PostMapping("/issues_suggestions")
    public R<?> createIssue(@RequestBody @Valid IssueVO issueVO) {
        issueService.createIssue(issueVO);
        return R.ok();
    }
}
