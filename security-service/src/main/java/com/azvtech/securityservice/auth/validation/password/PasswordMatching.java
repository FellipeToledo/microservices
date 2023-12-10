package com.azvtech.securityservice.auth.validation.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Fellipe Toledo
 */

@Constraint(validatedBy = PasswordMatchingValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatching {
    String password();
    String confirmPassword();
    String message() default "A senha e a confirmação da senha devem ser iguais!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        PasswordMatching[] value();
    }
}
