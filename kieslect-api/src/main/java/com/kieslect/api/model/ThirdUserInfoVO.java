package com.kieslect.api.model;

import com.kieslect.api.domain.ThirdLoginInfo;
import lombok.Data;

@Data
public class ThirdUserInfoVO extends ThirdLoginInfo {
    private Long id;
}
