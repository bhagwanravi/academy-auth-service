package com.example.academy.auth.client;

import com.example.academy.auth.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "user-service", configuration = FeignConfig.class)
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    Map<String, Object> getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/users/email/{email}")
    Map<String, Object> getUserByEmail(@PathVariable("email") String email);

    @PostMapping("/api/users")
    Map<String, Object> createUser(@RequestBody Map<String, Object> userRequest);

    @PostMapping("/api/users/{id}/activate")
    void activateUser(@PathVariable("id") Long id);

    @PostMapping("/api/users/{id}/deactivate")
    void deactivateUser(@PathVariable("id") Long id);
}
