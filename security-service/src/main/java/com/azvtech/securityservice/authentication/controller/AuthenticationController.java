package com.azvtech.securityservice.authentication.controller;

import com.azvtech.securityservice.authentication.IAuthenticationFacade;
import com.azvtech.securityservice.authentication.payload.request.AuthenticationRequest;
import com.azvtech.securityservice.authentication.payload.response.AuthenticationResponse;
import com.azvtech.securityservice.authentication.payload.response.MessageResponse;
import com.azvtech.securityservice.authentication.service.AuthenticationService;
import com.azvtech.securityservice.authentication.token.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author Fellipe Toledo
 */
@RestController
@RequestMapping("/api/v1/security")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private IAuthenticationFacade authenticationFacade;
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;

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

    @GetMapping("/users")
    public String getUser(@AuthenticationPrincipal UserDetails userDetails) {
        return "User Details: " + userDetails.getUsername();
    }

    @ResponseBody
    @GetMapping("/username")
    public String currentUserNameSimple() throws ExpiredJwtException {
        Authentication authentication = authenticationFacade.getAuthentication();
        return authentication.getName();
    }

}
