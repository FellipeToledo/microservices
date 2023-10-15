package com.azvtech.securityservice.auth.exception;

/**
 * @author Fellipe Toledo
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
