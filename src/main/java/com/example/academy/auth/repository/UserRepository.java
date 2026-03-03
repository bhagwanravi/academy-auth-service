package com.example.academy.auth.repository;

import com.example.academy.auth.dto.enums.Role;
import com.example.academy.auth.dto.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByStatus(UserStatus status);

    List<User> findByRole(Role role);

    List<User> findByTenantId(String tenantId);

    List<User> findByAcademyId(Long academyId);

    @Query("SELECT u FROM User u WHERE u.status = :status AND u.role = :role")
    List<User> findByStatusAndRole(UserStatus status, Role role);

    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId AND u.status = :status")
    List<User> findByTenantIdAndStatus(String tenantId, UserStatus status);

    boolean existsByEmailAndTenantId(String email, String tenantId);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.status = :status AND u.tenantId = :tenantId")
    List<User> findByRoleAndStatusAndTenantId(Role role, UserStatus status, String tenantId);

    Optional<User> findByRoleAndTenantId(Role role, String tenantId);

    void deleteByUser(User user);
}
