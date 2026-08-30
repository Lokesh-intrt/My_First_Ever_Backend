package com.example.project1.mappers;

import com.example.project1.DTOs.UserResponseDTO;
import com.example.project1.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapUserResponse {

    UserResponseDTO toDto(User user);
}
