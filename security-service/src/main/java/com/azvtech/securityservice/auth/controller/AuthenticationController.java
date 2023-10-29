package com.azvtech.securityservice.auth.controller;

import com.azvtech.securityservice.auth.payload.response.AuthenticationResponse;
import com.azvtech.securityservice.auth.service.AuthenticationService;
import com.azvtech.securityservice.auth.payload.request.AuthenticationRequest;
import com.azvtech.securityservice.auth.payload.request.RegisterRequest;
import com.azvtech.securityservice.user.jwt.JwtService;
import com.azvtech.securityservice.user.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author Fellipe Toledo
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        tokenService.refreshToken(request, response);
    }
}
