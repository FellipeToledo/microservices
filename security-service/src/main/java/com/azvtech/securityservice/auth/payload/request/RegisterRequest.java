package com.azvtech.securityservice.auth.payload.request;

import com.azvtech.securityservice.auth.validation.email.pattern.EmailPattern;
import com.azvtech.securityservice.auth.validation.password.PasswordMatching;
import com.azvtech.securityservice.auth.validation.password.StrongPassword;
import com.azvtech.securityservice.user.authorization.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Fellipe Toledo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatching(password = "Password", confirmPassword = "confirmPassword")
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String fullName;

    @NotBlank
    @Size(min = 4, max = 50)
    @EmailPattern
    private String email;

    @NotBlank
    @StrongPassword
    private String password;

    private String confirmPassword;

    private Role role;

}
