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
import java.util.List;

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

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(IssueDetailServiceImpl.class);

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
        // 先处理图片回复再处理消息回复
        if (issueDetailReplyVO.getAdminReplyimagePaths() != null && issueDetailReplyVO.getAdminReplyimagePaths().size() > 0){
            logger.info("issueId:{},管理员回复客户消息包含图片，数量：{}" ,issueDetailReplyVO.getIssueId(),issueDetailReplyVO.getAdminReplyimagePaths().size());
            List<IssueDetail> issueDetails = issueDetailReplyVO.getAdminReplyimagePaths().stream().map(imagePath -> {
                IssueDetail issueDetail = new IssueDetail();
                BeanUtil.copyProperties(issueDetailReplyVO, issueDetail);
                issueDetail.setIssueMsg(null);//消息置为空，消息会单独存储一条
                issueDetail.setFilePath(imagePath);//图片地址
                issueDetail.setIssueFileType(1);//文件类型：Image
                issueDetail.setCreateTime(time);
                issueDetail.setUpdateTime(time);
                issueDetail.setIssueUserId(0);
                issueDetail.setIssueStatus(1);
                return issueDetail;
            }).toList();
            saveBatch(issueDetails);
        }
        if (issueDetailReplyVO.getIssueMsg() == null && issueDetailReplyVO.getIssueFileType() == 0){
            // 这里是针对管理员只上传图片，不回复消息的情况进行拦截
            logger.info("issueId:{},管理员回复客户消息为空，不回复消息",issueDetailReplyVO.getIssueId());
            return 0;
        }
        IssueDetail issueDetail = new IssueDetail();
        BeanUtil.copyProperties(issueDetailReplyVO, issueDetail);
        issueDetail.setCreateTime(time);
        issueDetail.setUpdateTime(time);
        issueDetail.setIssueUserId(0);
        issueDetail.setIssueStatus(1);
        return save(issueDetail) == true ? 1 : 0;
    }
}
