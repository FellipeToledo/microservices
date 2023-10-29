package com.azvtech.securityservice.auth.validation.email.account;
import com.azvtech.securityservice.user.detail.User;

import lombok.Getter;
import lombok.Setter;

import org.springframework.context.ApplicationEvent;

/**
 * @author Fellipe Toledo
 */
@Getter
@Setter
public class AccountValidationEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;

    public AccountValidationEvent(User user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
