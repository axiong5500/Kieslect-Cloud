package com.kieslect.user.domain.vo;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class IssueVO {
    private Integer type;
    private String description;
    private String imagePaths;
    @Email
    private String contactEmail;
    private String shareLog;
    private String account;
}
