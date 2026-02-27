package com.example.academy.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ApprovalRequest {
    @NotBlank(message = "Super admin ID is required")
    private String superAdminId;
    
    private String comments;
}
