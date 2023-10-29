package com.azvtech.securityservice.auth.service;

import com.azvtech.securityservice.auth.payload.response.AuthenticationResponse;
import com.azvtech.securityservice.auth.exception.UserAlreadyExistsException;
import com.azvtech.securityservice.auth.payload.request.AuthenticationRequest;
import com.azvtech.securityservice.auth.payload.request.RegisterRequest;
import com.azvtech.securityservice.user.authorization.Role;
import com.azvtech.securityservice.auth.jwt.JwtService;
import com.azvtech.securityservice.auth.token.TokenService;
import com.azvtech.securityservice.user.detail.User;
import com.azvtech.securityservice.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Fellipe Toledo
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public User register(RegisterRequest request) {
        Optional<User> userEmail = userRepository.findByEmail((request.getEmail()));
        if (userEmail.isPresent()){
            throw new UserAlreadyExistsException(
                    "Usuário com o email "+request.getEmail() + " já cadastrado");
        }
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(true)
                .role(Role.USER)
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        tokenService.saveUserToken(savedUser, jwtToken);
        AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
        return user;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        tokenService.revokeAllUserTokens(user);
        tokenService.saveNewUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }
}
