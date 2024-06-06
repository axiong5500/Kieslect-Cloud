package com.kieslect.user.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.user.domain.Issue;
import com.kieslect.user.domain.vo.IssueVO;
import com.kieslect.user.enums.IssueTypeEnum;
import com.kieslect.user.mapper.IssueMapper;
import com.kieslect.user.service.IIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author kieslect
 * @since 2024-03-29
 */
@Service
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IIssueService {

    @Autowired
    private IssueMapper issueMapper;

    @Override
    public void createIssue(IssueVO issueVO) {
        Issue entity = Issue.builder()
                // 生成一个问题编码 ,后续可能需要有一定生成规则
                .issueNo(IdUtil.fastSimpleUUID())
                .userId(issueVO.getUserId())
                .description(issueVO.getDescription())
                .contactEmail(issueVO.getContactEmail())
                .imagePaths(issueVO.getImagePaths())
                .shareLog(issueVO.getShareLog())
                .type(IssueTypeEnum.getEnumByCode(issueVO.getType()).getCode())
                .issueStatus(0)
                .issueDealUser(0)
                .createTime(Instant.now().getEpochSecond())
                .updateTime(Instant.now().getEpochSecond())
                .build();
        this.save(entity);
    }
}
