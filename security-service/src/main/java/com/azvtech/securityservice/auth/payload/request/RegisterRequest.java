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
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @EmailPattern
    private String email;

    @NotBlank
    @StrongPassword
    private String password;

    @NotBlank
    private String confirmPassword;

    private Role role;
}
