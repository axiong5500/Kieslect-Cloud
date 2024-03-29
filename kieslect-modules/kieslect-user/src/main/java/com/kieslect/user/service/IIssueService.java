package com.kieslect.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.user.domain.Issue;
import com.kieslect.user.domain.vo.IssueVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-03-29
 */
public interface IIssueService extends IService<Issue> {

    void createIssue(IssueVO issueVO);
}
