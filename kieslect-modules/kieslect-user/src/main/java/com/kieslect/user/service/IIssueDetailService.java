package com.kieslect.user.service;

import com.kieslect.user.domain.IssueDetail;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.user.domain.vo.IssueDetailCreateVO;
import com.kieslect.user.domain.vo.IssueDetailReplyVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author kieslect
 * @since 2024-11-25
 */
public interface IIssueDetailService extends IService<IssueDetail> {

    int createIssueDetail(IssueDetailCreateVO issueDetailVO);

    int replyIssueDetail(IssueDetailReplyVO issueDetailReplyVO);
}
