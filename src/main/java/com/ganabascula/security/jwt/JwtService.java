package com.ganabascula.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "ganabascula_secret_key_super_segura_2026";

    public String generarToken(String cedula) {

        return Jwts.builder()

                .subject(cedula)

                .issuedAt(new Date())

                .expiration(
                        new Date(System.currentTimeMillis() + 1000 * 60 * 60)
                )

                .signWith(getSigningKey(), SignatureAlgorithm.HS256)

                .compact();
    }

    public String extractCedula(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, String cedula) {

        final String cedulaExtraida = extractCedula(token);

        return cedulaExtraida.equals(cedula)
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {

        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {

        final Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();
    }

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

}