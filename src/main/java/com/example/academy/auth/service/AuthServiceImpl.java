package com.example.academy.auth.service;

import com.example.academy.auth.dto.response.AuthResponse;
import com.example.academy.auth.dto.request.LoginRequest;
import com.example.academy.auth.dto.request.RegisterRequest;
import com.example.academy.auth.entity.RefreshToken;
import com.example.academy.auth.entity.User;
import com.example.academy.auth.enums.Role;
import com.example.academy.auth.enums.UserStatus;
import com.example.academy.auth.repository.RefreshTokenRepository;
import com.example.academy.auth.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public String register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Check if email exists in tenant
        if (userRepository.existsByEmailAndTenantId(request.getEmail(), request.getTenantId())) {
            throw new RuntimeException("Email already exists in this tenant");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .status(UserStatus.PENDING_APPROVAL)
                .phoneNumber(request.getPhoneNumber())
                .academyId(request.getAcademyId())
                .tenantId(request.getTenantId())
                .build();

        userRepository.save(user);

        // Publish user registration event
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_REGISTERED");
        event.put("userId", user.getId());
        event.put("email", user.getEmail());
        event.put("role", user.getRole());
        event.put("tenantId", user.getTenantId());
        event.put("academyId", user.getAcademyId());
        
        kafkaTemplate.send("user-events", event);
        log.info("User registration event published for user: {}", user.getEmail());

        return "Registration successful. Waiting for approval.";
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new RuntimeException("Account not approved yet");
        }

        String accessToken = jwtService.generateToken(user);
        String refreshTokenStr = jwtService.generateRefreshToken(user);

        // Save refresh token
        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenStr)
                .user(user)
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        refreshTokenRepository.save(refreshToken);

        // Publish login event
        Map<String, Object> event = new HashMap<>();
        event.put("eventType", "USER_LOGIN");
        event.put("userId", user.getId());
        event.put("email", user.getEmail());
        event.put("tenantId", user.getTenantId());
        event.put("academyId", user.getAcademyId());
        
        kafkaTemplate.send("user-events", event);
        log.info("User login event published for user: {}", user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenStr)
                .role(user.getRole().name())
                .message("Login successful")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .tenantId(user.getTenantId())
                .academyId(user.getAcademyId())
                .build();
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token expired");
        }

        User user = token.getUser();
        String newAccessToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)
                .role(user.getRole().name())
                .message("Token refreshed")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .tenantId(user.getTenantId())
                .academyId(user.getAcademyId())
                .build();
    }

    @Override
    @Transactional
    public void logout(String token) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            refreshTokenRepository.deleteByUser(user);
            
            // Publish logout event
            Map<String, Object> event = new HashMap<>();
            event.put("eventType", "USER_LOGOUT");
            event.put("userId", user.getId());
            event.put("email", user.getEmail());
            event.put("tenantId", user.getTenantId());
            
            kafkaTemplate.send("user-events", event);
            log.info("User logout event published for user: {}", user.getEmail());
        }
    }
}
