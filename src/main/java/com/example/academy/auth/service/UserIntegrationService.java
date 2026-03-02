package com.example.academy.auth.service;

import com.example.academy.auth.client.UserServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserIntegrationService {

    private final UserServiceClient userServiceClient;

    public Optional<Map<String, Object>> getUserById(Long userId) {
        try {
            log.info("Fetching user with ID: {} from user-service", userId);
            Map<String, Object> user = userServiceClient.getUserById(userId);
            return Optional.of(user);
        } catch (Exception e) {
            log.error("Error fetching user with ID {}: {}", userId, e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<Map<String, Object>> getUserByEmail(String email) {
        try {
            log.info("Fetching user with email: {} from user-service", email);
            Map<String, Object> user = userServiceClient.getUserByEmail(email);
            return Optional.of(user);
        } catch (Exception e) {
            log.error("Error fetching user with email {}: {}", email, e.getMessage());
            return Optional.empty();
        }
    }

    public boolean activateUser(Long userId) {
        try {
            log.info("Activating user with ID: {}", userId);
            userServiceClient.activateUser(userId);
            return true;
        } catch (Exception e) {
            log.error("Error activating user with ID {}: {}", userId, e.getMessage());
            return false;
        }
    }

    public boolean deactivateUser(Long userId) {
        try {
            log.info("Deactivating user with ID: {}", userId);
            userServiceClient.deactivateUser(userId);
            return true;
        } catch (Exception e) {
            log.error("Error deactivating user with ID {}: {}", userId, e.getMessage());
            return false;
        }
    }

    public Optional<Map<String, Object>> createUser(Map<String, Object> userRequest) {
        try {
            log.info("Creating new user in user-service");
            Map<String, Object> createdUser = userServiceClient.createUser(userRequest);
            return Optional.of(createdUser);
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
