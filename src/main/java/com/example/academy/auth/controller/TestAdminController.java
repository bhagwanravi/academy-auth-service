package com.example.academy.auth.controller;

import com.example.academy.auth.dto.request.ApprovalRequest;
import com.example.academy.auth.dto.request.RegisterRequest;
import com.example.academy.auth.dto.response.ApprovalResponse;
import com.example.academy.auth.dto.response.AuthResponse;
import com.example.academy.auth.enums.Role;
import com.example.academy.auth.service.AdminApprovalService;
import com.example.academy.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Test Admin API", description = "Test endpoints for admin approval email functionality")
public class TestAdminController {

    private final AdminApprovalService adminApprovalService;
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new admin (for testing email)")
    public ResponseEntity<String> registerAdmin(@RequestBody RegisterRequest request) {
        log.info("Testing admin registration for: {}", request.getEmail());
        
        // Set role to ADMIN for testing
        RegisterRequest adminRequest = RegisterRequest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(Role.ADMIN)
                .phoneNumber(request.getPhoneNumber())
                .academyId(request.getAcademyId())
                .tenantId(request.getTenantId())
                .build();

        String result = authService.register(adminRequest);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pending")
    @Operation(summary = "Get pending admin requests (for testing)")
    public ResponseEntity<List<ApprovalResponse>> getPendingAdmins(
            @RequestParam String tenantId) {
        log.info("Testing get pending admins for tenant: {}", tenantId);
        
        List<ApprovalResponse> pendingAdmins = adminApprovalService.getPendingAdminRequests(tenantId);
        return ResponseEntity.ok(pendingAdmins);
    }

    @PostMapping("/approve")
    @Operation(summary = "Approve an admin (for testing email)")
    public ResponseEntity<ApprovalResponse> approveAdmin(
            @RequestParam String adminId,
            @RequestBody ApprovalRequest request) {
        log.info("Testing admin approval for adminId: {}", adminId);
        
        ApprovalResponse response = adminApprovalService.approveAdmin(adminId, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reject")
    @Operation(summary = "Reject an admin (for testing email)")
    public ResponseEntity<ApprovalResponse> rejectAdmin(
            @RequestParam String adminId,
            @RequestBody ApprovalRequest request) {
        log.info("Testing admin rejection for adminId: {}", adminId);
        
        ApprovalResponse response = adminApprovalService.rejectAdmin(adminId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/super-admin-email")
    @Operation(summary = "Get super admin email for tenant (for testing)")
    public ResponseEntity<String> getSuperAdminEmail(@RequestParam String tenantId) {
        log.info("Testing get super admin email for tenant: {}", tenantId);
        
        String superAdminEmail = adminApprovalService.findSuperAdminEmail(tenantId);
        return ResponseEntity.ok(superAdminEmail);
    }

    // Test data endpoints for easy testing
    @GetMapping("/test-data/register-admin")
    @Operation(summary = "Register test admin with predefined data")
    public ResponseEntity<String> registerTestAdmin() {
        log.info("Registering test admin with predefined data");
        
        RegisterRequest testAdmin = RegisterRequest.builder()
                .name("Test Admin")
                .email("admin@example.com")
                .password("password123")
                .role(Role.ADMIN)
                .phoneNumber("1234567890")
                .academyId(1L)
                .tenantId("test-tenant")
                .build();

        String result = authService.register(testAdmin);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/test-data/approve-test-admin")
    @Operation(summary = "Approve test admin with predefined data")
    public ResponseEntity<ApprovalResponse> approveTestAdmin() {
        log.info("Approving test admin with predefined data");
        
        ApprovalRequest request = ApprovalRequest.builder()
                .superAdminId("super-admin-123")
                .comments("Approved for testing")
                .build();

        ApprovalResponse response = adminApprovalService.approveAdmin("test-admin-123", request);
        return ResponseEntity.ok(response);
    }
}
