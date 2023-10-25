package com.azvtech.securityservice.auth;

import com.azvtech.securityservice.auth.validation.email.pattern.EmailPattern;
import com.azvtech.securityservice.auth.validation.password.PasswordMatching;
import com.azvtech.securityservice.auth.validation.password.StrongPassword;
import com.azvtech.securityservice.user.Role;
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
@PasswordMatching(
        password = "password",
        confirmPassword = "confirmPassword",
        message = "A senha e a confirmação da senha devem ser iguais!")

public class RegisterRequest {
    private String firstname;
    private String lastname;
    @EmailPattern
    private String email;
    @StrongPassword
    private String password;
    private String confirmPassword;
    private Role role;
}
