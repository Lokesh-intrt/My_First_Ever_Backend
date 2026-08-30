package com.example.project1.controllers;

import com.example.project1.DTOs.UserLoginDTO;
import com.example.project1.DTOs.UserRegisterDTO;
import com.example.project1.repositories.UserRepository;
import com.example.project1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/user")
    ResponseEntity<String> registerUser(@Valid @RequestBody UserRegisterDTO userRegisterDTO)
    {
        return ResponseEntity.ok(userService.registerUser(userRegisterDTO));
    }

    @PostMapping("/login/user")
    ResponseEntity<String> loginUser(@Valid @RequestBody UserLoginDTO userLoginDTO) throws Exception {
        return ResponseEntity.ok(userService.loginUser(userLoginDTO));
    }
}
