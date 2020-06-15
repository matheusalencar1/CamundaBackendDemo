package com.camunda.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "camunda")
public class CamundaProperties {
    private String baseUrl;
    private String userProcessDefinitionId;
    private String userIdVariable;
}
