package com.kieslect.device.handler;

import com.kieslect.common.core.domain.R;
import org.slf4j.Logger;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public R<?> handleNullPointerException(NullPointerException ex) {
        logger.error("NullPointerException exception occurred: ", ex);
        Map<String, Object> errors = new HashMap<>();
        // 获取异常中的字段名称
        errors.put("error", "空指针异常: " + ex.getMessage());
        return R.fail(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public R<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        logger.error("Method argument type mismatch exception occurred: ", ex);
        Map<String, Object> errors = new HashMap<>();
        String fieldName = ex.getName();
        String errorMessage = "参数类型不匹配: " + ex.getValue() + " 无法转换为 " + ex.getRequiredType().getSimpleName();
        errors.put(fieldName, errorMessage);
        return R.fail(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("MethodArgumentNotValidException exception occurred: ", ex);
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return R.fail(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public R<?> handleValidationExceptions(HttpMessageNotReadableException ex) {
        logger.error("handleValidationExceptions exception occurred: ", ex);
        Map<String, Object> errors = new HashMap<>();
        // 获取异常中的字段名称
        String fieldName = extractFieldName(ex.getCause().getMessage());
        errors.put(fieldName, "数据格式输入有误！");
        return R.fail(errors);
    }

    // 从异常消息中提取字段名称
    private String extractFieldName(String message) {
        // 使用正则表达式匹配异常消息中的字段名称
        Pattern pattern = Pattern.compile("through reference chain: .*\\[\"(\\w+)\"\\]");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            // 如果无法提取字段名称，则返回默认值
            return "Unknown field";
        }
    }
}
