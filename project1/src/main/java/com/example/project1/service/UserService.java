package com.example.project1.service;

import com.example.project1.DTOs.UserLoginDTO;
import com.example.project1.DTOs.UserRegisterDTO;
import com.example.project1.DTOs.UserResponseDTO;
import com.example.project1.mappers.MapUserResponse;
import com.example.project1.model.User;
import com.example.project1.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;

    @Autowired
    MapUserResponse mapUserResponse;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
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

    public UserResponseDTO getUserInfo(Long id)
    {
        User user = userRepository.findById(id).orElseThrow(()->new EntityNotFoundException("User"));

        return mapUserResponse.toDto(user);
    }
}
