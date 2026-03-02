package com.example.academy.auth.controller;

import com.example.academy.auth.dto.request.ApprovalRequest;
import com.example.academy.auth.dto.response.ApprovalResponse;
import com.example.academy.auth.service.AdminApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-approval")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin Approval", description = "Admin Approval APIs")
public class AdminApprovalController {

    private final AdminApprovalService adminApprovalService;

    @GetMapping("/pending")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Get all pending admin requests for a tenant")
    public ResponseEntity<List<ApprovalResponse>> getPendingAdmins(@RequestParam String tenantId) {
        log.info("Fetching pending admin requests for tenant: {}", tenantId);
        List<ApprovalResponse> pendingAdmins = adminApprovalService.getPendingAdminRequests(tenantId);
        return ResponseEntity.ok(pendingAdmins);
    }

    @PostMapping("/approve/{adminId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Approve an admin request")
    public ResponseEntity<ApprovalResponse> approveAdmin(
            @PathVariable String adminId,
            @Valid @RequestBody ApprovalRequest request) {
        log.info("Approving admin with ID: {}", adminId);
        
        // Set the current super admin ID if not provided
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (request.getSuperAdminId() == null || request.getSuperAdminId().isEmpty()) {
            // You might need to extract user ID from authentication
            // For now, we'll use the provided ID
        }
        
        ApprovalResponse response = adminApprovalService.approveAdmin(adminId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reject/{adminId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Reject an admin request")
    public ResponseEntity<ApprovalResponse> rejectAdmin(
            @PathVariable String adminId,
            @Valid @RequestBody ApprovalRequest request) {
        log.info("Rejecting admin with ID: {}", adminId);
        
        ApprovalResponse response = adminApprovalService.rejectAdmin(adminId, request);
        return ResponseEntity.ok(response);
    }
}
