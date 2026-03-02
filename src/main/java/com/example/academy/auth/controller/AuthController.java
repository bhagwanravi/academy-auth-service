package com.example.academy.auth.controller;

import com.example.academy.auth.dto.response.AuthResponse;
import com.example.academy.auth.dto.request.LoginRequest;
import com.example.academy.auth.dto.request.RegisterRequest;
import com.example.academy.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request for email: {} in tenant: {}", request.getEmail(), request.getTenantId());
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/register")
    @Operation(summary = "Register a new admin and send approval request to super admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        log.info("Admin registration request for email: {} in tenant: {}", request.getEmail(), request.getTenantId());
        String response = authService.registerAdmin(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/approve")
    @Operation(summary = "Approve admin registration (Super Admin only)")
    public ResponseEntity<String> approveAdmin(@RequestParam String adminEmail,
                                            @RequestParam String approvalToken) {
        log.info("Admin approval request for email: {}", adminEmail);
        String response = authService.approveAdmin(adminEmail, approvalToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/reject")
    @Operation(summary = "Reject admin registration (Super Admin only)")
    public ResponseEntity<String> rejectAdmin(@RequestParam String adminEmail,
                                            @RequestParam String rejectionToken,
                                            @RequestParam(required = false) String reason) {
        log.info("Admin rejection request for email: {}", adminEmail);
        String response = authService.rejectAdmin(adminEmail, rejectionToken, reason);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/admin/pending")
    @Operation(summary = "Get pending admin approvals (Super Admin only)")
    public ResponseEntity<?> getPendingAdmins() {
        log.info("Fetching pending admin registrations");
        return ResponseEntity.ok(authService.getPendingAdmins());
    }

    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<AuthResponse> refresh(@RequestParam String refreshToken) {
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        authService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/validate")
    @Operation(summary = "Validate token")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        // This endpoint can be used by API Gateway to validate tokens
        return ResponseEntity.ok(true); // Simplified for now
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
