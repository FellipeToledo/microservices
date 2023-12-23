package com.azvtech.securityservice.user.controller;

import com.azvtech.securityservice.result.Result;
import com.azvtech.securityservice.user.ChangePasswordRequest;
import com.azvtech.securityservice.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * @author Fellipe Toledo
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PatchMapping
    public Result changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal connectedUser
    ) {
        return service.changePassword(request, connectedUser);
    }

    @GetMapping("/authenticated")
    public Principal userAuthenticated( Principal connectedUser){
        return service.userAuthenticated(connectedUser);
    }
}
