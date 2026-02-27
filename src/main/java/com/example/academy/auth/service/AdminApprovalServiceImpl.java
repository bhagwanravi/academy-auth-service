package com.example.academy.auth.service;

import com.example.academy.auth.dto.request.ApprovalRequest;
import com.example.academy.auth.dto.response.ApprovalResponse;
import com.example.academy.auth.entity.User;
import com.example.academy.auth.enums.Role;
import com.example.academy.auth.enums.UserStatus;
import com.example.academy.auth.repository.UserRepository;
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

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Override
    public List<ApprovalResponse> getPendingAdminRequests(String tenantId) {
        List<User> pendingAdmins = userRepository.findByRoleAndStatusAndTenantId(
                Role.ADMIN, UserStatus.PENDING_APPROVAL, tenantId);
        
        return pendingAdmins.stream()
                .map(this::convertToApprovalResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ApprovalResponse approveAdmin(String adminId, ApprovalRequest request) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is not an admin");
        }
        
        if (admin.getStatus() != UserStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Admin is not in pending approval status");
        }
        
        // Update admin status
        admin.setStatus(UserStatus.ACTIVE);
        userRepository.save(admin);
        
        // Get super admin details
        User superAdmin = userRepository.findById(request.getSuperAdminId())
                .orElseThrow(() -> new RuntimeException("Super admin not found"));
        
        // Send approval notification to admin
        emailService.sendAdminApprovalNotification(
                admin.getEmail(),
                admin.getName(),
                true,
                superAdmin.getName()
        );
        
        // Send notification to super admin
        emailService.sendAdminApprovedNotificationToSuperAdmin(
                superAdmin.getEmail(),
                admin.getName(),
                admin.getEmail()
        );
        
        log.info("Admin {} approved by super admin {}", admin.getEmail(), superAdmin.getEmail());
        
        return convertToApprovalResponse(admin);
    }

    @Override
    @Transactional
    public ApprovalResponse rejectAdmin(String adminId, ApprovalRequest request) {
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        if (admin.getRole() != Role.ADMIN) {
            throw new RuntimeException("User is not an admin");
        }
        
        if (admin.getStatus() != UserStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Admin is not in pending approval status");
        }
        
        // Update admin status
        admin.setStatus(UserStatus.REJECTED);
        userRepository.save(admin);
        
        // Get super admin details
        User superAdmin = userRepository.findById(request.getSuperAdminId())
                .orElseThrow(() -> new RuntimeException("Super admin not found"));
        
        // Send rejection notification to admin
        emailService.sendAdminApprovalNotification(
                admin.getEmail(),
                admin.getName(),
                false,
                superAdmin.getName()
        );
        
        log.info("Admin {} rejected by super admin {}", admin.getEmail(), superAdmin.getEmail());
        
        return convertToApprovalResponse(admin);
    }

    @Override
    public String findSuperAdminEmail(String tenantId) {
        return userRepository.findByRoleAndTenantId(Role.SUPER_ADMIN, tenantId)
                .map(User::getEmail)
                .orElseThrow(() -> new RuntimeException("No super admin found for tenant: " + tenantId));
    }

    private ApprovalResponse convertToApprovalResponse(User user) {
        return ApprovalResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .tenantId(user.getTenantId())
                .academyId(user.getAcademyId())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
