package com.azvtech.securityservice.auth.validation.email.pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Fellipe Toledo
 */
public class EmailPatternValidator implements ConstraintValidator<EmailPattern, String> {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@"+"[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";
    @Override
    public void initialize(final EmailPattern constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String email, ConstraintValidatorContext context){
        return (validateEmail(email));
    }
    private boolean validateEmail(final String email) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
