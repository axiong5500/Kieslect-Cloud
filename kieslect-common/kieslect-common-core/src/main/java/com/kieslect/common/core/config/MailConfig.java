package com.kieslect.common.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mail")
@Data
public class MailConfig {
    private String host;
    private int port;
    private String username;
    private String password;
}
