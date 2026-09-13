package com.example.project1.service;

import com.example.project1.DTOs.UserLoginDTO;
import com.example.project1.DTOs.UserRegisterDTO;
import com.example.project1.DTOs.UserResponseDTO;
import com.example.project1.DTOs.UserUpdateDTO;
import com.example.project1.mappers.MapUserResponse;
import com.example.project1.mappers.MapUserUpdate;
import com.example.project1.model.User;
import com.example.project1.repositories.UserRepository;
import com.example.project1.exceptions.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
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

    public void registerUser(UserRegisterDTO userRegisterDTO)
    {
        String encodedPassword = passwordEncoder.encode(userRegisterDTO.getPassword());
        User user = new User();
        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(encodedPassword);
        user.setRole(User.Roles.ROLE_USER);

        userRepository.save(user);
    }

    public String loginUser(UserLoginDTO userLoginDTO){

        String token = authenticationService.login(userLoginDTO.getEmail(),userLoginDTO.getPassword());
        log.info("User:{} successfully logged in",userLoginDTO.getEmail());

        return token;
    }

    public UserResponseDTO getUserInfo(String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User"));
        return mapUserResponse.toDto(user);
    }

    @PreAuthorize("hasRole('USER')")
    public User updateUser(UserUpdateDTO userUpdateDTO, String email)
    {
        User user = userRepository.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("user"));

        mapUserUpdate.updateFromDto(userUpdateDTO, user);
        userRepository.save(user);

        return user;
    }

}
