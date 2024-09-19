package com.kieslect.common.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "mail")
@Data
public class MailConfig {
    private Map<String, MailAccountConfig> accounts;
    @Data
    public static class MailAccountConfig {
        private String host;
        private int port;
        private String username;
        private String password;
        private String personal;
    }
}
