package com.example.project1.controllers;

import com.example.project1.DTOs.UserResponseDTO;
import com.example.project1.DTOs.UserUpdateDTO;
import com.example.project1.model.User;
import com.example.project1.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get")
    public ResponseEntity<UserResponseDTO> getUserInfo(Principal principal)
    {
        String email = principal.getName();

        return ResponseEntity.ok(userService.getUserInfo(email));
    }

    @PatchMapping("/update")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserUpdateDTO userUpdateDTO,Principal principal)
    {
        User user = userService.updateUser(userUpdateDTO,principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}

