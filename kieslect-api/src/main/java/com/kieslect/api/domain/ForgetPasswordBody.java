package com.kieslect.api.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgetPasswordBody {
    @NotBlank
    private String account;
    @NotBlank
    private String code;
    @NotBlank
    private String password;
    @NotNull
    private Byte appName;

}
