package com.example.project1.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.Authentication;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class JwtService {

    private final Algorithm algorithm;
    private final long refreshTime;
    private final long expireTime;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${token_refresh_time}") long refreshTime, @Value("${token_expire_time}") long expireTime) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.refreshTime = refreshTime;
        this.expireTime = expireTime;
    }

    public String generateToken(Authentication authentication) {
       return JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(Calendar.getInstance().getTime())
                .withExpiresAt(new Date(System.currentTimeMillis()+expireTime))
                .sign(algorithm);
    }

    public String generateRefreshToken(Authentication authentication)
    {
        return JWT.create()
                .withSubject(authentication.getName())
                .withIssuedAt(Calendar.getInstance().getTime())
                .withExpiresAt(new Date(System.currentTimeMillis()+refreshTime))
                .sign(algorithm);
    }

    public String getName(String token)
    {
        return JWT.require(algorithm).build().verify(token).getSubject();
    }

    public Boolean isValid (String token)
    {
        try
        {
            JWT.require(algorithm).build().verify(token);
            return true;
        }

        catch(JWTVerificationException exception)
        {
            return false;
        }
    }
}
