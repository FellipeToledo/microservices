package com.azvtech.securityservice.user.repository;

import com.azvtech.securityservice.auth.payload.request.RegisterRequest;
import com.azvtech.securityservice.user.detail.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Fellipe Toledo
 */

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByFullName(String fullName);
    Optional<User> findByEmail(String email);

}
