package com.azvtech.securityservice.register.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author Fellipe Toledo
 */
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StrongPassword {
    String message() default "The password must contain at least 8 characters, a combination of uppercase letters, lowercase letters, numbers and special characters.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
