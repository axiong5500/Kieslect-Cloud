package com.kieslect.outapi.handler;

import com.kieslect.common.core.domain.R;
import com.kieslect.outapi.exception.ApiNotOpenException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiNotOpenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public R<?> handleApiNotOpenException(ApiNotOpenException ex) {
        return R.fail(HttpStatus.FORBIDDEN.value(), ex.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public R<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String errorMessage = "保存数据时发生违反数据完整性约束的异常。";
        // 可以根据实际情况进一步处理异常信息
        // 尝试从异常中获取更具体的信息
        if (ex.getCause() instanceof SQLException) {
            SQLException sqlException = (SQLException) ex.getCause();
            String message = sqlException.getMessage();
            String[] parts = message.split("'"); // 根据具体数据库错误信息格式进行分割
            if (parts.length > 1) {
                String fieldName = parts[1]; // 获取引起异常的字段名
                errorMessage += " 引起异常的字段：" + fieldName;
            }
        }
        return R.fail(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public R<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
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
        // 获取异常中的字段名称
        String fieldName = extractFieldName(ex.getCause().getMessage());
       String errorMessage =  String.format("字段：%s%s", fieldName,"数据格式输入有误！");
        return R.fail(errorMessage);
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
