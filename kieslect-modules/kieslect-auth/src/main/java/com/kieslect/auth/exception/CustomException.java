package com.kieslect.auth.exception;

import com.kieslect.common.core.enums.ResponseCodeEnum;

public class CustomException extends RuntimeException{
    private final ResponseCodeEnum responseCode;

    public CustomException(ResponseCodeEnum responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }

    public ResponseCodeEnum getResponseCode() {
        return responseCode;
    }
}
