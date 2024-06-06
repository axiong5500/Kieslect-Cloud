package com.kieslect.user.controller;

import com.kieslect.common.core.domain.LoginUserInfo;
import com.kieslect.common.core.domain.R;
import com.kieslect.common.security.service.TokenService;
import com.kieslect.user.domain.vo.IssueVO;
import com.kieslect.user.service.IIssueService;
import jakarta.servlet.http.HttpServletRequest;
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
    @Autowired
    private TokenService tokenService;
    @PostMapping("/issues_suggestions")
    public R<?> createIssue(HttpServletRequest request, @RequestBody  IssueVO issueVO) {
        LoginUserInfo loginUser = tokenService.getLoginUser(request);
        issueVO.setUserId(loginUser.getId());
        issueService.createIssue(issueVO);
        return R.ok();
    }
}
