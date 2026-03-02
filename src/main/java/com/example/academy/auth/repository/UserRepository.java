package com.example.academy.auth.repository;

import com.example.academy.auth.entity.User;
import com.example.academy.auth.enums.Role;
import com.example.academy.auth.enums.UserStatus;
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
}
