package com.kieslect.user.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author kieslect
 * @since 2024-11-25
 */
@Data
public class IssueDetailReplyVO extends IssueDetailCreateVO implements Serializable {
    List<String> adminReplyimagePaths;
    private Integer issueStatus;
}
