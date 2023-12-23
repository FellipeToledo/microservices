package com.azvtech.securityservice.user.service;

import com.azvtech.securityservice.authentication.AuthenticationFacade;
import com.azvtech.securityservice.register.payload.RegisterRequest;
import com.azvtech.securityservice.result.Result;
import com.azvtech.securityservice.result.StatusCode;
import com.azvtech.securityservice.user.ChangePasswordRequest;
import com.azvtech.securityservice.user.authorization.Role;
import com.azvtech.securityservice.user.detail.User;
import com.azvtech.securityservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

/**
 * @author Fellipe Toledo
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final AuthenticationFacade authenticationFacade;

    public User buildUser (RegisterRequest request) {
        return User
                .builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .role(Role.USER)
                .build();
    }

    public Principal userAuthenticated(Principal principal) {

        return authenticationFacade.getAuthentication();
    }

    public Result changePassword(ChangePasswordRequest request, Principal connectedUser) {

        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            return new Result(false, StatusCode.CONFLICT, "Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            return new Result(false, StatusCode.CONFLICT, "Password are not the same");
        }

        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // save the new password
        repository.save(user);

        return new Result(true, StatusCode.SUCCESS, LocalDateTime.now(), "Password changed successfully", connectedUser);
    }
}
