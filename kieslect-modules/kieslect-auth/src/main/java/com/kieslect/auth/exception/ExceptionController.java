package com.kieslect.auth.exception;


import com.kieslect.common.core.domain.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public R<?> handlerException(CustomException e){
        //异常返回false，Result是上一篇接口返回对象。
        return R.fail(e.getResponseCode());
    }

}
