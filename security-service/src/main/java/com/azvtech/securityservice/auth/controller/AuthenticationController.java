package com.azvtech.securityservice.auth.controller;

import com.azvtech.securityservice.auth.exception.result.Result;
import com.azvtech.securityservice.auth.exception.result.StatusCode;
import com.azvtech.securityservice.auth.payload.request.AuthenticationRequest;
import com.azvtech.securityservice.auth.payload.request.RegisterRequest;
import com.azvtech.securityservice.auth.payload.response.AuthenticationResponse;
import com.azvtech.securityservice.auth.payload.response.MessageResponse;
import com.azvtech.securityservice.auth.service.AuthenticationService;
import com.azvtech.securityservice.auth.token.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Fellipe Toledo
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Result register(
            @Valid
            @RequestBody RegisterRequest registerRequest,
            final HttpServletRequest httpServletRequest
    ) {
        return authenticationService.register(registerRequest, httpServletRequest);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid
            @RequestBody
            AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        tokenService.refreshToken(request, response);
        return ResponseEntity.ok(new MessageResponse("token successfully updated"));
    }

    @GetMapping("/validate")
    public String validate(@RequestParam("token") String token) {
        return tokenService.validateToken(token);
    }

    @GetMapping("/user")
    public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return "User Details: " + userDetails.getUsername();
    }
}
