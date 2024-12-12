package com.kieslect.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kieslect.user.domain.Issue;
import com.kieslect.user.domain.vo.IssueListVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kieslect
 * @since 2024-03-29
 */
public interface IssueMapper extends BaseMapper<Issue> {

    List<IssueListVO> listJoinMsgLevel(Long userId);
}
