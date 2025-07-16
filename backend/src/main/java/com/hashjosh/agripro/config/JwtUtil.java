package com.hashjosh.agripro.config;

import com.hashjosh.agripro.config.dto.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private final JwtProperties jwtProperties;
    private SecretKey secretKey;


    public JwtUtil(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }


    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes());
    }


    public String generateToken(String username, List<String> roles, List<String> authorities) {
        Map<String, Object> claims = new HashMap<>();
        Date expiryDate = Date.from(Instant.now().plusMillis(Long.parseLong(jwtProperties.expiration())));


       claims.put("roles", roles);
       claims.put("permissions", authorities);

        System.out.println("Claim roles in JwtUtl class:::: " + roles);
        System.out.println("Claim permissions in JwtUtl class:::: " + authorities);
        System.out.printf("Jwt secret key ::: %s%n", jwtProperties.secret());

        System.out.println("Jwt expiration:: " + jwtProperties.expiration());

        return  Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .claims(claims)
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
