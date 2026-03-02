package com.example.academy.auth.service;

import com.example.academy.auth.dto.request.LoginRequest;
import com.example.academy.auth.dto.request.RegisterRequest;
import com.example.academy.auth.dto.response.AuthResponse;

public interface AuthService {
    String register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
    void logout(String token);

    // Admin-specific methods
    String registerAdmin(RegisterRequest request);
    String approveAdmin(String adminEmail, String approvalToken);
    String rejectAdmin(String adminEmail, String rejectionToken, String reason);
    Object getPendingAdmins();
}
