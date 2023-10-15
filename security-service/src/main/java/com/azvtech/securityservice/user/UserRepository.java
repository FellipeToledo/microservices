package com.azvtech.securityservice.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Fellipe Toledo
 */

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

}
