package com.kieslect.auth.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgetPasswordBody {
    @NotBlank
    private String account;
    @NotBlank
    private String code;
    @NotBlank
    private String password;
}
