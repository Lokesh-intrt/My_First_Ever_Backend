package com.example.project1.controllers;

import com.example.project1.DTOs.UserResponseDTO;
import com.example.project1.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<UserResponseDTO> getUserInfo(@PathVariable Long id)
    {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }
}
