package com.azvtech.securityservice.exception;

/**
 * @author Fellipe Toledo
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
