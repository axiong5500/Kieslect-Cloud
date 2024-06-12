package com.kieslect.auth.utils;

import com.kieslect.auth.exception.CustomException;
import com.kieslect.common.core.enums.ResponseCodeEnum;
import com.kieslect.common.core.utils.EmailUtils;

public class ValidationUtils {

    /**
     * 判断是否是邮箱
     * @param str
     * @return  true 是邮箱 false 不是邮箱
     */
    public static boolean isValidEmail(String str){
        try {
            return EmailUtils.isEmail(str);
        } catch (RuntimeException ex) {
            throw new CustomException(ResponseCodeEnum.EMAIL_FORMAT_ERROR);
        }
    }
}
