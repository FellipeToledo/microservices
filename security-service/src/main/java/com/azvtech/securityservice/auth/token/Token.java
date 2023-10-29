package com.azvtech.securityservice.auth.token;

import com.azvtech.securityservice.user.detail.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * @author Fellipe Toledo
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {
    @Id
    @GeneratedValue
    private Integer id;
    private String token;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;
    private boolean expired;
    public Date expirationTime;
    private boolean revoked;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Token(String token, User user) {
        super();
        this.token = token;
        this.user = user;
    }

}
