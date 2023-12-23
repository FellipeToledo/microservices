package com.azvtech.securityservice.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Fellipe Toledo
 */
@Getter
@Setter
@Builder
public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
