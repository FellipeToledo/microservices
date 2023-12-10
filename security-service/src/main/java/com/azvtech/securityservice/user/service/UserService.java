package com.azvtech.securityservice.user.service;

import com.azvtech.securityservice.auth.payload.request.RegisterRequest;
import com.azvtech.securityservice.user.authorization.Role;
import com.azvtech.securityservice.user.detail.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Fellipe Toledo
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    public User buildUser (RegisterRequest request) {
        return User
                .builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .role(Role.USER)
                .build();
    }
}
