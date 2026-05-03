package com.FootballTeam.footballTeam.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration.ms}")
    private int jwtExpirationMs;

    // Genera la chiave
    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // Genera un JWT dato l'oggetto Authentication
    public String generateToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Estrae l'username dal JWT
    public String getUsernameFromJwt(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // Validazione del JWT (Invalid,Expired,Unsupported e Empty)
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.out.println("Token JWT non valido: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("Token JWT scaduto: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Token JWT non supportato: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Token JWT vuoto: " + e.getMessage());
        }
        return false;
    }
}