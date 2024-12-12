package com.kieslect.user.domain.vo;

import lombok.Data;

@Data
public class IssueDetailListVO {
    private static final long serialVersionUID = 1L;

    /**
     * 问题ID，见 t_issue 表 ID
     */
    private Long issueId;

    /**
     * 人员，0：系统人员，1：用户
     */
    private Integer issueUserId;

    /**
     * 回复的消息
     */
    private String issueMsg;

    /**
     * 回复的文件路径
     */
    private String filePath;

    /**
     * 上传的文件类型，0：无文件，1：image，2：voice，3：video
     */
    private Integer issueFileType;

    /**
     * 创建时间，时间戳，秒
     */
    private Long createTime;

}
