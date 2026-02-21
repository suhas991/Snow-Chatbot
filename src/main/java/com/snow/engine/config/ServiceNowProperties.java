package com.snow.engine.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "servicenow")
@Data
public class ServiceNowProperties {
    private String baseUrl;
    private String username;
    private String password;
    private String table;
    private String maxLimit;
    private List<String> responseFields;
}


