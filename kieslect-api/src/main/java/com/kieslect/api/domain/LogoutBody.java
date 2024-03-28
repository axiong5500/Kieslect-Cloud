package com.kieslect.api.domain;

import lombok.Data;

@Data
public class LogoutBody {

    private String account;

    private String password;

    private Byte appName;

}
