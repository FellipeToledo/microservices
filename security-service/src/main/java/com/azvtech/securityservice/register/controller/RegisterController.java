package com.azvtech.securityservice.register.controller;

import com.azvtech.securityservice.register.payload.RegisterRequest;
import com.azvtech.securityservice.register.service.RegisterService;
import com.azvtech.securityservice.result.Result;
import com.azvtech.securityservice.result.StatusCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author Fellipe Toledo
 */
@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
@CrossOrigin
public class RegisterController {
    private final RegisterService registerService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public Result register(
            @Valid
            @RequestBody RegisterRequest registerRequest,
            final HttpServletRequest httpServletRequest
    ) {
        var authenticatedUser = registerService.register(registerRequest, httpServletRequest);
        return new Result(true, StatusCode.CREATED, LocalDateTime.now(), "User registered successfully! Verify your email to activate your account", authenticatedUser);
    }
}
