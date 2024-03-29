package com.kieslect.user.service.impl;

import com.kieslect.user.domain.Issue;
import com.kieslect.user.mapper.IssueMapper;
import com.kieslect.user.service.IIssueService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
