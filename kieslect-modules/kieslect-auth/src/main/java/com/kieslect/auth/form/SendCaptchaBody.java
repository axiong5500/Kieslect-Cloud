package com.kieslect.auth.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendCaptchaBody {

    @NotBlank
    private String toEmail;
    /**
     * 验证码类型，0：注册，1：忘记密码，2：修改密码，3：修改邮箱
     */
    @NotBlank
    private String emailType;

    /**
     * 应用名
     */
    @NotNull
    private Byte appName;
}
