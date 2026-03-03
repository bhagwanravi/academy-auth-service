package com.example.academy.auth.service;

import com.example.academy.auth.dto.response.AuthResponse;
import com.example.academy.auth.dto.request.LoginRequest;
import com.example.academy.auth.dto.request.RegisterRequest;
import com.example.academy.auth.dto.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    // In-memory storage for demo purposes (replace with database in production)
    private final Map<String, Map<String, Object>> users = new ConcurrentHashMap<>();
    private final Map<String, String> refreshTokens = new ConcurrentHashMap<>();

    @Override
    public String register(RegisterRequest request) {
        log.info("Registration request for email: {}", request.getEmail());
        
        // Check if user already exists
        if (users.containsKey(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        
        // Store user (in production, save to database)
        Map<String, Object> user = new HashMap<>();
        user.put("email", request.getEmail());
        user.put("name", request.getName());
        user.put("password", request.getPassword()); // In production, encrypt this
        user.put("role", request.getRole() != null ? request.getRole().name() : "USER");
        user.put("tenantId", request.getTenantId());
        user.put("academyId", request.getAcademyId());
        user.put("phoneNumber", request.getPhoneNumber());
        user.put("active", true);
        
        users.put(request.getEmail(), user);
        
        log.info("User registered successfully: {}", request.getEmail());
        return "Registration successful";
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());
        
        // Find user (in production, query database)
        Map<String, Object> user = users.get(request.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        // Check password (in production, use encrypted password)
        if (!user.get("password").equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        
        // Check if user is active
        if (!(Boolean) user.get("active")) {
            throw new RuntimeException("Account is not active");
        }
        
        // Generate tokens (in production, use JWT)
        String accessToken = "access-token-" + System.currentTimeMillis();
        String refreshToken = "refresh-token-" + System.currentTimeMillis();
        
        // Store refresh token
        refreshTokens.put(refreshToken, request.getEmail());
        
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role((String) user.get("role"))
                .message("Login successful")
                .email(request.getEmail())
                .name((String) user.get("name"))
                .tenantId((String) user.get("tenantId"))
                .academyId(user.get("academyId") != null ? (Long) user.get("academyId") : null)
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        log.info("Token refresh request");
        
        // Validate refresh token (in production, use JWT validation)
        String email = refreshTokens.get(refreshToken);
        if (email == null) {
            throw new RuntimeException("Invalid refresh token");
        }
        
        // Find user
        Map<String, Object> user = users.get(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        
        // Generate new access token (in production, use JWT)
        String newAccessToken = "new-access-token-" + System.currentTimeMillis();
        
        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .role((String) user.get("role"))
                .message("Token refreshed successfully")
                .email(email)
                .name((String) user.get("name"))
                .tenantId((String) user.get("tenantId"))
                .academyId(user.get("academyId") != null ? (Long) user.get("academyId") : null)
                .build();
    }

    @Override
    public void logout(String token) {
        log.info("Logout request");
        
        // Remove refresh token (in production, also invalidate access token)
        if (token != null && refreshTokens.containsKey(token)) {
            refreshTokens.remove(token);
            log.info("User logged out successfully");
        } else {
            log.info("Logout request processed (token not found or null)");
        }
    }
    
    // Helper method to get all users (for testing)
    public Map<String, Map<String, Object>> getAllUsers() {
        return users;
    }
    
    // Helper method to get all refresh tokens (for testing)
    public Map<String, String> getAllRefreshTokens() {
        return refreshTokens;
    }
}
