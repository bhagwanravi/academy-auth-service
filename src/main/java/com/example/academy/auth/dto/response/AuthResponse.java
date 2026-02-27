package com.example.academy.auth.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String role;
    private String message;
    private Long userId;
    private String name;
    private String email;
    private String tenantId;
    private Long academyId;
}
