package com.azvtech.securityservice.authentication.token;

import com.azvtech.securityservice.authentication.jwt.JwtService;
import com.azvtech.securityservice.authentication.payload.response.AuthenticationResponse;
import com.azvtech.securityservice.user.detail.User;
import com.azvtech.securityservice.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Fellipe Toledo
 */
@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(true)
                .expired(jwtService.isTokenExpired(jwtToken))
                .expirationTime(jwtService.extractExpiration(jwtToken))
                .build();
        tokenRepository.save(token);
    }

    public void saveNewUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(jwtService.isTokenExpired(jwtToken))
                .expirationTime(jwtService.extractExpiration(jwtToken))
                .build();
        tokenRepository.save(token);
    }

    public void revokeAllUserTokens(User user) {
        var validUserToken = tokenRepository.findAllValidTokensByUser((user.getId()));
        if (validUserToken.isEmpty())
            return;
        validUserToken.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserToken);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public String validateToken(String theToken) {
        Optional<Token> token = tokenRepository.findByToken(theToken);
        if(token.isEmpty()){
            return "Invalid verification token";
        }
        User user = token.get().getUser();
        if (!jwtService.isTokenValid(theToken, user)) {
            return "Verification link already expired," +
                    " Please, click the link below to receive a new verification link";
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "account successfully verified";
    }
}
