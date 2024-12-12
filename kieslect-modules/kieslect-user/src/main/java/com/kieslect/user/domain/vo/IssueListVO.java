package com.kieslect.user.domain.vo;

import lombok.Data;

@Data
public class IssueListVO {
    private Long issueId;
    private String description;
    private Long createTime;
    // 是否已读
    private boolean readStatus;
    // 紧急程度，0-一般不显示，1-内部显示红点，2-外部显示红点
    private Integer issueLevel;
    //消息类型，1:问题与反馈，2：广告位
    private Integer msgType;
    // 广告图片地址
    private String imageUrl;
    // 广告跳转地址
    private String redirectUrl;
}
