package com.example.academy.auth.service;

import com.example.academy.auth.dto.response.AuthResponse;
import com.example.academy.auth.dto.request.LoginRequest;
import com.example.academy.auth.dto.request.RegisterRequest;
// import com.example.academy.auth.entity.RefreshToken; // TODO: Uncomment when User entity is created
// import com.example.academy.auth.entity.User; // TODO: Uncomment when User entity is created
import com.example.academy.auth.enums.Role;
import com.example.academy.auth.enums.UserStatus;
// import com.example.academy.auth.repository.RefreshTokenRepository; // TODO: Uncomment when User entity is created
// import com.example.academy.auth.repository.UserRepository; // TODO: Uncomment when User entity is created
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    // private final UserRepository userRepository; // TODO: Uncomment when User entity is created
    private final PasswordEncoder passwordEncoder;
    // private final JwtService jwtService; // TODO: Uncomment when User entity is created
    private final AuthenticationManager authenticationManager;
    // private final RefreshTokenRepository refreshTokenRepository; // TODO: Uncomment when User entity is created
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final EmailService emailService;

    @Override
    @Transactional
    public String register(RegisterRequest request) {
        // TODO: Uncomment when User entity is created
        // if (userRepository.existsByEmail(request.getEmail())) {
        //     throw new RuntimeException("Email already exists");
        // }

        // // Check if email exists in tenant
        // if (userRepository.existsByEmailAndTenantId(request.getEmail(), request.getTenantId())) {
        //     throw new RuntimeException("Email already exists in this tenant");
        // }

        // User user = User.builder()
        //         .name(request.getName())
        //         .email(request.getEmail())
        //         .password(passwordEncoder.encode(request.getPassword()))
        //         .role(request.getRole())
        //         .status(UserStatus.PENDING_APPROVAL)
        //         .phoneNumber(request.getPhoneNumber())
        //         .academyId(request.getAcademyId())
        //         .tenantId(request.getTenantId())
        //         .build();

        // userRepository.save(user);

        // Send email notification to super admin if registering as admin
        if (request.getRole() == Role.ADMIN) {
            try {
                // TODO: Uncomment when User entity is created
                // String superAdminEmail = userRepository.findByRoleAndTenantId(Role.SUPER_ADMIN, request.getTenantId())
                //         .map(User::getEmail)
                //         .orElse(null);
                
                // For testing email functionality - using hardcoded values
                String superAdminEmail = "bhagwanravi97@gmail.com";
                String adminName = request.getName();
                String adminEmail = request.getEmail();
                
                if (superAdminEmail != null) {
                    emailService.sendAdminRegistrationNotification(
                            superAdminEmail,
                            adminName,
                            adminEmail,
                            request.getTenantId()
                    );
                    log.info("Admin registration notification sent to super admin: {}", superAdminEmail);
                } else {
                    log.warn("No super admin found for tenant: {}", request.getTenantId());
                }
            } catch (Exception e) {
                log.error("Failed to send admin registration notification", e);
                // Continue with registration even if email fails
            }
        }

        // TODO: Uncomment when User entity is created
        // // Publish user registration event
        // Map<String, Object> event = new HashMap<>();
        // event.put("eventType", "USER_REGISTERED");
        // event.put("userId", user.getId());
        // event.put("email", user.getEmail());
        // event.put("role", user.getRole());
        // event.put("tenantId", user.getTenantId());
        // event.put("academyId", user.getAcademyId());
        // 
        // kafkaTemplate.send("user-events", event);
        // log.info("User registration event published for user: {}", user.getEmail());

        return "Registration successful. Waiting for approval.";
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        // TODO: Implement when User entity is created
        // For now, return a placeholder response
        log.info("Login attempt for email: {}", request.getEmail());
        return AuthResponse.builder()
                .accessToken("placeholder-access-token")
                .refreshToken("placeholder-refresh-token")
                .role("USER")
                .message("Login successful - placeholder implementation")
                .userId(1L)
                .name("Test User")
                .email(request.getEmail())
                .tenantId("test-tenant")
                .academyId(1L)
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        // TODO: Implement when User entity is created
        // For now, return a placeholder response
        log.info("Token refresh attempt");
        return AuthResponse.builder()
                .accessToken("new-placeholder-access-token")
                .refreshToken(refreshToken)
                .role("USER")
                .message("Token refreshed - placeholder implementation")
                .userId(1L)
                .name("Test User")
                .email("test@example.com")
                .tenantId("test-tenant")
                .academyId(1L)
                .build();
    }

    @Override
    @Transactional
    public void logout(String token) {
        // TODO: Implement when User entity is created
        // For now, just log the logout attempt
        log.info("Logout attempt with token: {}", token);
    }

    @Override
    @Transactional
    public String registerAdmin(RegisterRequest request) {
        log.info("Admin registration request for email: {}", request.getEmail());
        
        // Set role to ADMIN for admin registration
        request.setRole(Role.ADMIN);
        
        // For now, use the same registration logic but with admin-specific email
        String result = register(request);
        
        // Send approval email to super admin
        try {
            String superAdminEmail = "bhagwanravi97@gmail.com"; // Hardcoded for now
            String approvalLink = generateApprovalLink(request.getEmail(), "APPROVAL");
            String rejectionLink = generateApprovalLink(request.getEmail(), "REJECTION");
            
            emailService.sendAdminApprovalRequest(
                superAdminEmail,
                request.getName(),
                request.getEmail(),
                request.getTenantId(),
                approvalLink,
                rejectionLink
            );
            
            log.info("Admin approval request sent to super admin: {}", superAdminEmail);
        } catch (Exception e) {
            log.error("Failed to send admin approval request", e);
        }
        
        return "Admin registration successful. Approval request sent to super admin.";
    }

    @Override
    @Transactional
    public String approveAdmin(String adminEmail, String approvalToken) {
        log.info("Approving admin registration for email: {}", adminEmail);
        
        // TODO: Validate approval token and update admin status in database
        // For now, just send confirmation emails
        
        try {
            // Send approval confirmation to admin
            emailService.sendAdminApprovalConfirmation(adminEmail, "APPROVED");
            
            // Send notification to super admin about approval
            emailService.sendAdminApprovalNotification("bhagwanravi97@gmail.com", adminEmail, "APPROVED");
            
            log.info("Admin approval completed for: {}", adminEmail);
            return "Admin approved successfully.";
        } catch (Exception e) {
            log.error("Failed to approve admin: {}", adminEmail, e);
            throw new RuntimeException("Failed to approve admin: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public String rejectAdmin(String adminEmail, String rejectionToken, String reason) {
        log.info("Rejecting admin registration for email: {}, reason: {}", adminEmail, reason);
        
        // TODO: Validate rejection token and update admin status in database
        // For now, just send rejection emails
        
        try {
            // Send rejection notification to admin
            emailService.sendAdminApprovalConfirmation(adminEmail, "REJECTED");
            
            // Send notification to super admin about rejection
            emailService.sendAdminApprovalNotification("bhagwanravi97@gmail.com", adminEmail, "REJECTED");
            
            log.info("Admin rejection completed for: {}", adminEmail);
            return "Admin rejected successfully.";
        } catch (Exception e) {
            log.error("Failed to reject admin: {}", adminEmail, e);
            throw new RuntimeException("Failed to reject admin: " + e.getMessage());
        }
    }

    @Override
    public Object getPendingAdmins() {
        log.info("Fetching pending admin registrations");
        
        // TODO: Implement database query to get pending admins
        // For now, return a placeholder response
        return Map.of(
            "pendingAdmins", java.util.List.of(),
            "message", "No pending admin registrations found"
        );
    }

    private String generateApprovalLink(String email, String action) {
        // Generate approval/rejection link with token
        String token = java.util.UUID.randomUUID().toString();
        return String.format("http://localhost:8081/api/auth/admin/%s?email=%s&token=%s", 
                           action.toLowerCase(), email, token);
    }
}
