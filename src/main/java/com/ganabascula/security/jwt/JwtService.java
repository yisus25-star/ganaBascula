package com.ganabascula.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.JwtException;

import io.jsonwebtoken.security.Keys;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.util.Date;

import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY =

            "8d2f9c1e7a6b4d3f9e2c7a1b5f8d6c3e4a9b7c2d1f5e8a6b3c9d7e1f4a2b6c8";

    private static final long
            JWT_EXPIRATION = 1000 * 60 * 60 * 2;

    public String generarToken(
            String cedula
    ) {

        return Jwts.builder()

                .subject(cedula)

                .issuedAt(
                        new Date()
                )

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + JWT_EXPIRATION
                        )
                )

                .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    public String extractCedula(
            String token
    ) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }

    public boolean isTokenValid(
            String token,
            String cedula
    ) {

        try {

            final String cedulaExtraida =
                    extractCedula(token);

            return cedulaExtraida.equals(cedula)
                    && !isTokenExpired(token);

        } catch (Exception e) {

            return false;
        }
    }

    private boolean isTokenExpired(
            String token
    ) {

        return extractExpiration(token)
                .before(new Date());
    }

    private Date extractExpiration(
            String token
    ) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }

    private <T> T extractClaim(

            String token,

            Function<Claims, T>
                    claimsResolver
    ) {

        final Claims claims =
                extractAllClaims(token);

        return claimsResolver
                .apply(claims);
    }

    private Claims extractAllClaims(
            String token
    ) {

        try {

            return Jwts.parser()

                    .verifyWith(
                            getSigningKey()
                    )

                    .build()

                    .parseSignedClaims(token)

                    .getPayload();

        } catch (JwtException e) {

            throw new RuntimeException(
                    "Token JWT inválido o expirado"
            );
        }
    }

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes()
        );
    }
}