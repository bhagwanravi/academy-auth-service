package com.example.academy.auth.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Configuration
@EnableDiscoveryClient
@ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "true", matchIfMissing = true)
public class DiscoveryConfig {
    // This configuration will only be enabled when eureka.client.enabled is true
}
