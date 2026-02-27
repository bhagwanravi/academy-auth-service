package com.example.academy.auth.service;

import com.example.academy.auth.dto.request.ApprovalRequest;
import com.example.academy.auth.dto.response.ApprovalResponse;
import com.example.academy.auth.entity.User;
import com.example.academy.auth.enums.Role;
import com.example.academy.auth.enums.UserStatus;
import com.example.academy.auth.repository.UserRepository;

import java.util.List;

public interface AdminApprovalService {
    List<ApprovalResponse> getPendingAdminRequests(String tenantId);
    ApprovalResponse approveAdmin(String adminId, ApprovalRequest request);
    ApprovalResponse rejectAdmin(String adminId, ApprovalRequest request);
    String findSuperAdminEmail(String tenantId);
}
