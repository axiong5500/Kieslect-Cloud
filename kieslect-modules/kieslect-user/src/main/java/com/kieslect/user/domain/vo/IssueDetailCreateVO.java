package com.kieslect.user.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author kieslect
 * @since 2024-11-25
 */
@Data
public class IssueDetailCreateVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 问题ID，见 t_issue 表 ID
     */
    private Long issueId;


    /**
     * 回复的消息
     */
    private String issueMsg;

    /**
     * 回复的文件路径
     */
    private String filePath;

    /**
     * 上传的文件类型，0：文本，1：image，:2：voice，3：video
     */
    private Integer issueFileType;


}
