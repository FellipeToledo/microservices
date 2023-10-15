package com.azvtech.securityservice.token;

import com.azvtech.securityservice.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Fellipe Toledo
 */
@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;
    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(true)
                .expired(true)
                .build();
        tokenRepository.save(token);
    }
    public void saveNewUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
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
}
