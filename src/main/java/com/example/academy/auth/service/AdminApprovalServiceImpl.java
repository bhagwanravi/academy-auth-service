package com.example.academy.auth.service;

import com.example.academy.auth.dto.request.ApprovalRequest;
import com.example.academy.auth.dto.response.ApprovalResponse;
// import com.example.academy.auth.entity.User; // TODO: Uncomment when User entity is created
import com.example.academy.auth.enums.Role;
import com.example.academy.auth.enums.UserStatus;
// import com.example.academy.auth.repository.UserRepository; // TODO: Uncomment when User entity is created
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminApprovalServiceImpl implements AdminApprovalService {

    // private final UserRepository userRepository; // TODO: Uncomment when User entity is created
    private final EmailService emailService;

    @Override
    public List<ApprovalResponse> getPendingAdminRequests(String tenantId) {
        // TODO: Uncomment when User entity is created
        // List<User> pendingAdmins = userRepository.findByRoleAndStatusAndTenantId(
        //         Role.ADMIN, UserStatus.PENDING_APPROVAL, tenantId);
        // 
        // return pendingAdmins.stream()
        //         .map(this::convertToApprovalResponse)
        //         .collect(Collectors.toList());
        return List.of(); // Placeholder
    }

    @Override
    @Transactional
    public ApprovalResponse approveAdmin(String adminId, ApprovalRequest request) {
        // TODO: Uncomment when User entity is created
        // User admin = userRepository.findById(adminId)
        //         .orElseThrow(() -> new RuntimeException("Admin not found"));
        // 
        // if (admin.getRole() != Role.ADMIN) {
        //     throw new RuntimeException("User is not an admin");
        // }
        // 
        // if (admin.getStatus() != UserStatus.PENDING_APPROVAL) {
        //     throw new RuntimeException("Admin is not in pending approval status");
        // }
        // 
        // // Update admin status
        // admin.setStatus(UserStatus.ACTIVE);
        // userRepository.save(admin);
        // 
        // // Get super admin details
        // User superAdmin = userRepository.findById(request.getSuperAdminId())
        //         .orElseThrow(() -> new RuntimeException("Super admin not found"));
        
        // For testing email functionality - using hardcoded values
        String adminEmail = "admin@example.com";
        String adminName = "Test Admin";
        String superAdminEmail = "superadmin@example.com";
        String superAdminName = "Test Super Admin";
        
        // Send approval notification to admin
        emailService.sendAdminApprovalNotification(
                adminEmail,
                adminName,
                true,
                superAdminName
        );
        
        // Send notification to super admin
        emailService.sendAdminApprovedNotificationToSuperAdmin(
                superAdminEmail,
                adminName,
                adminEmail
        );
        
        log.info("Admin {} approved by super admin {}", adminEmail, superAdminEmail);
        
        // TODO: Uncomment when User entity is created
        // return convertToApprovalResponse(admin);
        return ApprovalResponse.builder().build(); // Placeholder
    }

    @Override
    @Transactional
    public ApprovalResponse rejectAdmin(String adminId, ApprovalRequest request) {
        // TODO: Uncomment when User entity is created
        // User admin = userRepository.findById(adminId)
        //         .orElseThrow(() -> new RuntimeException("Admin not found"));
        // 
        // if (admin.getRole() != Role.ADMIN) {
        //     throw new RuntimeException("User is not an admin");
        // }
        // 
        // if (admin.getStatus() != UserStatus.PENDING_APPROVAL) {
        //     throw new RuntimeException("Admin is not in pending approval status");
        // }
        // 
        // // Update admin status
        // admin.setStatus(UserStatus.REJECTED);
        // userRepository.save(admin);
        // 
        // // Get super admin details
        // User superAdmin = userRepository.findById(request.getSuperAdminId())
        //         .orElseThrow(() -> new RuntimeException("Super admin not found"));
        
        // For testing email functionality - using hardcoded values
        String adminEmail = "admin@example.com";
        String adminName = "Test Admin";
        String superAdminName = "Test Super Admin";
        
        // Send rejection notification to admin
        emailService.sendAdminApprovalNotification(
                adminEmail,
                adminName,
                false,
                superAdminName
        );
        
        log.info("Admin {} rejected by super admin {}", adminEmail, "superadmin@example.com");
        
        // TODO: Uncomment when User entity is created
        // return convertToApprovalResponse(admin);
        return ApprovalResponse.builder().build(); // Placeholder
    }

    @Override
    public String findSuperAdminEmail(String tenantId) {
        // TODO: Uncomment when User entity is created
        // return userRepository.findByRoleAndTenantId(Role.SUPER_ADMIN, tenantId)
        //         .map(User::getEmail)
        //         .orElseThrow(() -> new RuntimeException("No super admin found for tenant: " + tenantId));
        return "superadmin@example.com"; // Placeholder for testing
    }

    // TODO: Uncomment when User entity is created
    // private ApprovalResponse convertToApprovalResponse(User user) {
    //     return ApprovalResponse.builder()
    //             .id(user.getId())
    //             .name(user.getName())
    //             .email(user.getEmail())
    //             .phoneNumber(user.getPhoneNumber())
    //             .role(user.getRole().name())
    //             .status(user.getStatus().name())
    //             .tenantId(user.getTenantId())
    //             .academyId(user.getAcademyId())
    //             .createdAt(user.getCreatedAt())
    //             .build();
    // }
}
