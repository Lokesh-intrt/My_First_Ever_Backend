package com.example.project1.service;

import com.example.project1.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RedisJwtBlackListing redisJwtBlackListing;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService, RedisJwtBlackListing redisJwtBlackListing) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.redisJwtBlackListing = redisJwtBlackListing;
    }

    public String login(String username, String password) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);

        return jwtService.generateToken(authentication);
    }

    public void blacklistJwtToken(String token)
    {
        String jti = jwtService.getId(token);
        Date expireTime = jwtService.getExpireTime(token);
        redisJwtBlackListing.blacklistJwt(jti, expireTime);
    }
}
