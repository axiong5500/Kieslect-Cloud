package com.kieslect.device.domain.vo;

import lombok.Data;

@Data
public class CustomBackgroundVO {
    String url;

    // 构造方法
    public CustomBackgroundVO(String url) {
        this.url = url;
    }
}
