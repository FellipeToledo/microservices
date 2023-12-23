package com.azvtech.securityservice.authentication.payload.request;

import com.azvtech.securityservice.register.validation.email.pattern.EmailPattern;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Fellipe Toledo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    @NotBlank
    @EmailPattern
    private String email;

    @NotBlank
    String password;
}
