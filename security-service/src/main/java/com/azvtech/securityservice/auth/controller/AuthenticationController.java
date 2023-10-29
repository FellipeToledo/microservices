package com.azvtech.securityservice.auth.controller;

import com.azvtech.securityservice.auth.payload.response.AuthenticationResponse;
import com.azvtech.securityservice.auth.payload.response.MessageResponse;
import com.azvtech.securityservice.auth.service.AuthenticationService;
import com.azvtech.securityservice.auth.payload.request.AuthenticationRequest;
import com.azvtech.securityservice.auth.payload.request.RegisterRequest;
import com.azvtech.securityservice.auth.jwt.JwtService;
import com.azvtech.securityservice.auth.token.Token;
import com.azvtech.securityservice.auth.token.TokenRepository;
import com.azvtech.securityservice.auth.token.TokenService;
import com.azvtech.securityservice.auth.validation.email.account.AccountValidationEvent;
import com.azvtech.securityservice.user.detail.User;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * @author Fellipe Toledo
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final HttpServletRequest servletRequest;
    private final ApplicationEventPublisher publisher;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final TokenRepository tokenRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> register(
            @Valid
            @RequestBody RegisterRequest registerRequest,
            final HttpServletRequest httpServletRequest
    ) {
        User user = authenticationService.register(registerRequest);
        publisher.publishEvent(new AccountValidationEvent(user, applicationUrl(httpServletRequest)));
        return ResponseEntity.ok(new MessageResponse("User registered successfully! Verify your email to activate your account"));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String jwtToken){
        String url = applicationUrl(servletRequest)+"/api/v1/auth/resend-verification-email?token="+jwtToken;

        Optional<Token> token = tokenRepository.findByToken(jwtToken);
        if (token.map(t -> t.getUser().isEnabled()).orElse(true)){
            return "This account has already been verified, please, login.";
        }

        boolean verificationResult = jwtService.validateJwtToken(jwtToken);
        if (verificationResult){
            return "Email verified successfully. Now you can login to your account";
        }
        return "Invalid verification email, <a href=\"" +url+"\"> Get a new verification link. </a>";
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        tokenService.refreshToken(request, response);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +request.getServerName()+ ":" +request.getServerPort()+request.getContextPath();
    }
}
