package com.kieslect.user.controller;


import com.kieslect.common.core.domain.R;
import com.kieslect.user.exception.CustomException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public R<?> handlerException(CustomException e){
        return R.fail(e.getResponseCode());
    }

}
