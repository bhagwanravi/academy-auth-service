package com.example.academy.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ApprovalRequest {
    @NotBlank(message = "Super admin ID is required")
    private String superAdminId;
    
    private String comments;
}
