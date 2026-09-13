package com.example.project1.controllers;

import com.example.project1.DTOs.TokenFactoryDTO;
import com.example.project1.DTOs.UserLoginDTO;
import com.example.project1.DTOs.UserRegisterDTO;
import com.example.project1.repositories.UserRepository;
import com.example.project1.security.OAuthSuccessHandler;
import com.example.project1.service.OAuthJWTTokenSevice;
import com.example.project1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final OAuthJWTTokenSevice oAuthJWTTokenSevice;

    public AuthController(UserService userService, OAuthJWTTokenSevice oAuthJWTTokenSevice) {
        this.userService = userService;
        this.oAuthJWTTokenSevice = oAuthJWTTokenSevice;
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestParam String refreshToken) {
        TokenFactoryDTO tokenFactoryDTO = oAuthJWTTokenSevice.refreshToken(refreshToken);
        if (tokenFactoryDTO.getRefreshToken() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(tokenFactoryDTO);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid access token");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDTO userLoginDTO) {
        return ResponseEntity.ok(userService.loginUser(userLoginDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserRegisterDTO userRegisterDTO)
    {
        userService.registerUser(userRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
