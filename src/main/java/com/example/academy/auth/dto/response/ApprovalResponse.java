package com.example.academy.auth.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ApprovalResponse {
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private String status;
    private String tenantId;
    private String academyId;
    private LocalDateTime createdAt;
}
