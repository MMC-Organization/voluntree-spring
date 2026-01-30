package com.voluntree.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.voluntree.backend.domain.CustomUserDetails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.springframework.security.config.Elements.JWT;

import io.lettuce.core.search.arguments.VectorFieldArgs.Algorithm;

@Service
public class TokenService {

    
    @Value("${JWT_SECRET}")
    private String secret;

    public String generateToken(CustomUserDetails user) {
        try {
            
            Algorithm algorithm = Algorithm.HMAC256(secret);
            
            return JWT.create()
                    .withIssuer("voluntree-api")
                    .withSubject(user.getUsername()) 
                    .withClaim("id", user.getUserId()) 
                    .withClaim("userType", user.getUserType().toString())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    private Instant genExpirationDate() {
        
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}