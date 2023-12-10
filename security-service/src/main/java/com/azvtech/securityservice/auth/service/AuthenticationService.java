package com.azvtech.securityservice.auth.service;
import com.azvtech.securityservice.auth.exception.UserAlreadyExistsException;
import com.azvtech.securityservice.auth.exception.result.Result;
import com.azvtech.securityservice.auth.exception.result.StatusCode;
import com.azvtech.securityservice.auth.jwt.JwtService;
import com.azvtech.securityservice.auth.payload.request.AuthenticationRequest;
import com.azvtech.securityservice.auth.payload.request.RegisterRequest;
import com.azvtech.securityservice.auth.payload.response.AuthenticationResponse;
import com.azvtech.securityservice.auth.token.TokenService;
import com.azvtech.securityservice.auth.validation.email.account.AccountValidationEvent;
import com.azvtech.securityservice.user.detail.User;
import com.azvtech.securityservice.user.repository.UserRepository;
import com.azvtech.securityservice.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Fellipe Toledo
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ApplicationEventPublisher publisher;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final UserService userService;
    private final UserRepository userRepository;

    public Result register(RegisterRequest request, final HttpServletRequest httpServletRequest) {
        Optional<User> userEmail = userRepository.findByEmail((request.getEmail()));
        if (userEmail.isPresent()){
            throw new UserAlreadyExistsException(request.getEmail());
        }
        var user = userService.buildUser(request);
        userRepository.save(user);
        publisher.publishEvent(new AccountValidationEvent(user, applicationUrl(httpServletRequest)));
        return new Result(true, StatusCode.CREATED, "User registered successfully! Verify your email to activate your account", user);
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

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +request.getServerName()+ ":" +request.getServerPort()+request.getContextPath();
    }
}
