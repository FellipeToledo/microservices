package com.azvtech.securityservice.auth.validation.email.account;
import com.azvtech.securityservice.auth.jwt.JwtService;

import com.azvtech.securityservice.auth.payload.response.MessageResponse;
import com.azvtech.securityservice.auth.token.TokenService;
import com.azvtech.securityservice.user.detail.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author Fellipe Toledo
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AccountValidationListener implements ApplicationListener<AccountValidationEvent> {
    private final JwtService jwtService;
    private final TokenService tokenService;
    private final JavaMailSender mailSender;
    private User user;

    @Override
    public void onApplicationEvent(AccountValidationEvent event) {

        // Get the newly registered user
        user = event.getUser();

        // create a verification token for the user
        var jwtToken = jwtService.generateToken(user);

        tokenService.saveUserToken(user, jwtToken);
        // Build the verification URL tobe sent to the user
        String url = event.getApplicationUrl()+"/api/v1/auth/validate?token="+jwtToken;

        // Send the e-mail
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        //Log
        String response = "User registered successfully! Verify your email to activate your account";
        log.info(response);

    }

    public void sendVerificationEmail (String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Smart Event Management";
        String mailContent = "<p> Hi, "+ user.getFullName()+ ", </p>"+
                "<p>Thank you for registering with us, "+"" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Smart Event Management";
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("smarteventmanagement@gmail.com", senderName);
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
