package com.example.academy.auth.service;

public interface EmailService {
    void sendAdminRegistrationNotification(String superAdminEmail, String adminName, String adminEmail, String tenantId);
    void sendAdminApprovalNotification(String adminEmail, String adminName, boolean approved, String superAdminName);
    void sendAdminApprovedNotificationToSuperAdmin(String superAdminEmail, String adminName, String adminEmail);
    
    // Additional methods for admin approval workflow
    void sendAdminApprovalRequest(String superAdminEmail, String adminName, String adminEmail, 
                                 String tenantId, String approvalLink, String rejectionLink);
    void sendAdminApprovalConfirmation(String adminEmail, String status);
    void sendAdminApprovalNotification(String superAdminEmail, String adminEmail, String status);
}
