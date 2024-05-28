package com.kieslect.user.domain.vo;

import lombok.Data;

@Data
public class IssueVO {
    private Integer type;
    private String description;
    private String imagePaths;
    private String contactEmail;
    private String shareLog;
    private String account;
}
