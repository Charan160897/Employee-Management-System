package com.ems.employee.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiration;

    public JwtService(
            @Value("${jwt.secret}")
            String secret,

            @Value("${jwt.expiration}")
            long expiration
    ) {
        byte[] keyBytes =
                Base64.getDecoder()
                        .decode(secret);

        this.secretKey =
                Keys.hmacShaKeyFor(
                        keyBytes
                );

        this.expiration = expiration;
    }

    public String generateToken(
            Long userId,
            String username,
            String role
    ) {

        Date now = new Date();

        Date expirationDate =
                new Date(
                        now.getTime()
                                + expiration
                );

        return Jwts.builder()
                .subject(username)

                .claim(
                        "userId",
                        userId
                )

                .claim(
                        "role",
                        role
                )

                .issuedAt(now)

                .expiration(
                        expirationDate
                )

                .signWith(
                        secretKey
                )

                .compact();
    }

    public Claims extractClaims(
            String token
    ) {
        return Jwts.parser()
                .verifyWith(
                        secretKey
                )
                .build()
                .parseSignedClaims(
                        token
                )
                .getPayload();
    }

    public String extractUsername(
            String token
    ) {
        return extractClaims(token)
                .getSubject();
    }

    public String extractRole(
            String token
    ) {
        return extractClaims(token)
                .get(
                        "role",
                        String.class
                );
    }

    public Long extractUserId(
            String token
    ) {
        Number userId =
                extractClaims(token)
                        .get(
                                "userId",
                                Number.class
                        );

        return userId.longValue();
    }

    public boolean isTokenValid(
            String token
    ) {
        try {

            Claims claims =
                    extractClaims(token);

            return claims
                    .getExpiration()
                    .after(
                            new Date()
                    );

        } catch (Exception exception) {

            return false;
        }
    }
}