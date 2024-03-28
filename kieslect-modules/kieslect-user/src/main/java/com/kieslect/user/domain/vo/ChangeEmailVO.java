package com.kieslect.user.domain.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangeEmailVO {
    @NotBlank
    String newEmail;
    String oldCode;
    @NotBlank
    String newCode;
}
