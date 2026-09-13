package com.example.project1.service;

import com.example.project1.DTOs.TokenFactoryDTO;
import com.example.project1.security.JwtService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class OAuthJWTTokenSevice {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public OAuthJWTTokenSevice(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public TokenFactoryDTO OAuthLogin(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        return new TokenFactoryDTO(jwtService.generateToken(authentication), jwtService.generateRefreshToken(authentication));
    }

    public TokenFactoryDTO refreshToken(String refreshToken)
    {
        if(jwtService.isValid(refreshToken))
        {
            String username = jwtService.getName(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

            String newToken = jwtService.generateToken(authenticationToken);

            return new TokenFactoryDTO(newToken, refreshToken);
        }

        return new TokenFactoryDTO();
    }
}
