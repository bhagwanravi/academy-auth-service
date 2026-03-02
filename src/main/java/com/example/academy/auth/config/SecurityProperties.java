package com.example.academy.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "app.security")
public class SecurityProperties {
    private List<String> publicPaths;
    private List<String> unauthorizedApiPaths;
    private List<String> adminApiPaths;
    private List<String> superadminApiPaths;
    private List<String> allowedOrigins;
    private List<String> allowedHeaders;
    private List<String> allowedMethods;
    private List<String> exposedHeaders;
    private boolean allowCredentials;
    private long maxAge;
    private CorsProperties cors;

    @Data
    public static class CorsProperties {
        private boolean enabled = true;
        private List<String> allowedOrigins;
        private List<String> allowedMethods;
        private List<String> allowedHeaders;
        private List<String> exposedHeaders;
        private boolean allowCredentials = true;
        private long maxAge = 3600;
    }
}
