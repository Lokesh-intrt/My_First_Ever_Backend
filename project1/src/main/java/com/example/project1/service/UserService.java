package com.example.project1.service;

import com.example.project1.DTOs.UserLoginDTO;
import com.example.project1.DTOs.UserRegisterDTO;
import com.example.project1.DTOs.UserResponseDTO;
import com.example.project1.DTOs.UserUpdateDTO;
import com.example.project1.mappers.MapUserResponse;
import com.example.project1.mappers.MapUserUpdate;
import com.example.project1.model.User;
import com.example.project1.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    private final MapUserResponse mapUserResponse;
    private final  MapUserUpdate mapUserUpdate;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService, MapUserResponse mapUserResponse, MapUserUpdate mapUserUpdate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
        this.mapUserResponse = mapUserResponse;
        this.mapUserUpdate = mapUserUpdate;
    }

    public String registerUser(UserRegisterDTO userRegisterDTO)
    {
        String encodedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        User user = new User();
        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(User.Roles.ROLE_USER);

        userRepository.save(user);

        return "You have been successfully registered";
    }

    public String loginUser(UserLoginDTO userLoginDTO) throws Exception {

        return authenticationService.login(userLoginDTO.getEmail(),userLoginDTO.getPassword());
    }

    public UserResponseDTO getUserInfo(String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("User"));
        return mapUserResponse.toDto(user);
    }

    @PreAuthorize("hasRole('USER')")
    public User updateUser(UserUpdateDTO userUpdateDTO, String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("user"));

        mapUserUpdate.updateFromDto(userUpdateDTO, user);
        userRepository.save(user);

        return user;
    }

}
