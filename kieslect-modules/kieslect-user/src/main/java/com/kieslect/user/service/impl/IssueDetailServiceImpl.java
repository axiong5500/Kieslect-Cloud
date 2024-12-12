package com.kieslect.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kieslect.user.domain.IssueDetail;
import com.kieslect.user.domain.vo.IssueDetailCreateVO;
import com.kieslect.user.domain.vo.IssueDetailReplyVO;
import com.kieslect.user.mapper.IssueDetailMapper;
import com.kieslect.user.service.IIssueDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-11-25
 */
@Service
public class IssueDetailServiceImpl extends ServiceImpl<IssueDetailMapper, IssueDetail> implements IIssueDetailService {

    @Override
    public int createIssueDetail(IssueDetailCreateVO issueDetailVO) {
        Long time = Instant.now().getEpochSecond();
        IssueDetail issueDetail = new IssueDetail();
        BeanUtil.copyProperties(issueDetailVO, issueDetail);
        issueDetail.setCreateTime(time);
        issueDetail.setUpdateTime(time);
        issueDetail.setIssueUserId(1);
        issueDetail.setIssueStatus(1);
        return save(issueDetail) == true ? 1 : 0;
    }

    @Override
    public int replyIssueDetail(IssueDetailReplyVO issueDetailReplyVO) {
        Long time = Instant.now().getEpochSecond();
        IssueDetail issueDetail = new IssueDetail();
        BeanUtil.copyProperties(issueDetailReplyVO, issueDetail);
        issueDetail.setCreateTime(time);
        issueDetail.setUpdateTime(time);
        issueDetail.setIssueUserId(0);
        issueDetail.setIssueStatus(1);
        return save(issueDetail) == true ? 1 : 0;
    }
}
