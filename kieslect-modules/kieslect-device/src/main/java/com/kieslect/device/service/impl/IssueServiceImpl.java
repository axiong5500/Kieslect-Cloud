package com.kieslect.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kieslect.device.domain.Issue;
import com.kieslect.device.mapper.IssueMapper;
import com.kieslect.device.service.IIssueService;
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
