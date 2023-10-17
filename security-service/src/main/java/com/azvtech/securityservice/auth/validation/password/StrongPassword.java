package com.azvtech.securityservice.auth.validation.password;

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
    String message() default "A senha deve conter no mínimo 8 caracteres, uma combinação de letras maiúsculas, " +
            "letras minúsculas, números e caracteres especiais.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
