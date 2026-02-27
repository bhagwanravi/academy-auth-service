package com.example.academy.auth.service;

public interface EmailService {
    void sendAdminRegistrationNotification(String superAdminEmail, String adminName, String adminEmail, String tenantId);
    void sendAdminApprovalNotification(String adminEmail, String adminName, boolean approved, String superAdminName);
    void sendAdminApprovedNotificationToSuperAdmin(String superAdminEmail, String adminName, String adminEmail);
}
