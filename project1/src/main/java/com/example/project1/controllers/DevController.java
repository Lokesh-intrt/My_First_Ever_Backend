package com.example.project1.controllers;

import com.example.project1.model.User;
import com.example.project1.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dev")
@PreAuthorize("hasRole('ADMIN')")
public class DevController {

    private final UserRepository userRepository;

    public DevController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PatchMapping("/promote/{id}")
    public ResponseEntity<String> promoteUser(@PathVariable Long id)
    {
        User user = userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("user"));

        user.setRole(User.Roles.ROLE_SELLER);

        userRepository.save(user);
        return ResponseEntity.ok("User promoted to Seller");
    }
}
