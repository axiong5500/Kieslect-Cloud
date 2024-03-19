package com.kieslect.auth.form;

import lombok.Data;

/**
 * 用户注册对象
 * 
 * @author kieslect
 */
@Data
public class RegisterBody extends LoginBody
{
    private String code;

    
    private Number registerType;
}
