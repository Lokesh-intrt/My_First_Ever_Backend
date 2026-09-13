package com.example.project1.security;

import com.example.project1.DTOs.TokenFactoryDTO;
import com.example.project1.model.User;
import com.example.project1.repositories.UserRepository;
import com.example.project1.service.OAuthJWTTokenSevice;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.core.user. OAuth2User;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final OAuthJWTTokenSevice oAuthJWTTokenSevice;
    public OAuthSuccessHandler(UserRepository userRepository, OAuthJWTTokenSevice oAuthJWTTokenSevice) {
        this.userRepository = userRepository;
        this.oAuthJWTTokenSevice = oAuthJWTTokenSevice;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email).orElseGet(()->{
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setPassword(UUID.randomUUID().toString());
            newUser.setRole(User.Roles.ROLE_USER);
            userRepository.save(newUser);

            return newUser;
        });


        TokenFactoryDTO tokenFactoryDTO = oAuthJWTTokenSevice.OAuthLogin(user.getEmail());

        response.sendRedirect("http://localhost:3000/oauth2/callback?token=" + tokenFactoryDTO.getAccessToken() + "&refresh=" + tokenFactoryDTO.getRefreshToken());
    }
}
