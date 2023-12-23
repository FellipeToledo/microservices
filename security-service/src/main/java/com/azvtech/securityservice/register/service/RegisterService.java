package com.azvtech.securityservice.register.service;

import com.azvtech.securityservice.register.payload.RegisterRequest;
import com.azvtech.securityservice.exception.UserAlreadyExistsException;
import com.azvtech.securityservice.register.validation.email.account.AccountValidationEvent;
import com.azvtech.securityservice.user.detail.User;
import com.azvtech.securityservice.user.repository.UserRepository;
import com.azvtech.securityservice.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Fellipe Toledo
 */
@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;

    public User register(RegisterRequest request, final HttpServletRequest httpServletRequest) {
        Optional<User> userEmail = userRepository.findByEmail((request.getEmail()));
        if (userEmail.isPresent()){
            throw new UserAlreadyExistsException(request.getEmail());
        }
        var user = userService.buildUser(request);
        userRepository.save(user);
        publisher.publishEvent(new AccountValidationEvent(user, applicationUrl(httpServletRequest)));
        return user;
    }
    private String applicationUrl(HttpServletRequest request) {
        return "http://" +request.getServerName()+ ":" +request.getServerPort()+request.getContextPath();
    }
}
