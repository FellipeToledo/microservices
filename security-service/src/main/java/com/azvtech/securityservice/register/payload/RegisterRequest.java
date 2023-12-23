package com.azvtech.securityservice.register.payload;

import com.azvtech.securityservice.register.validation.email.pattern.EmailPattern;
import com.azvtech.securityservice.register.validation.password.PasswordMatching;
import com.azvtech.securityservice.register.validation.password.StrongPassword;
import com.azvtech.securityservice.user.authorization.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author Fellipe Toledo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@PasswordMatching(password = "password", confirmPassword = "confirmPassword")
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String fullName;

    @NotBlank
    @Size(min = 4, max = 50)
    @EmailPattern
    private String email;

    @StrongPassword
    private String password;

    private String confirmPassword;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "auth_user_role", joinColumns = @JoinColumn(name = "auth_user_id"), inverseJoinColumns = @JoinColumn(name = "auth_role_id"))
    private Set<Role> roles;

}
