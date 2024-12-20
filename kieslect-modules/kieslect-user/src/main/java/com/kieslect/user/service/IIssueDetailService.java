package com.kieslect.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kieslect.user.domain.IssueDetail;
import com.kieslect.user.domain.vo.IssueDetailCreateVO;
import com.kieslect.user.domain.vo.IssueDetailReplyVO;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(rollbackFor = {Exception.class})
    int replyIssueDetail(IssueDetailReplyVO issueDetailReplyVO);
}
