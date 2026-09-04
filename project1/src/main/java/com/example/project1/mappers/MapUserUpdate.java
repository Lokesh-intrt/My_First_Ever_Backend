package com.example.project1.mappers;

import com.example.project1.DTOs.UserUpdateDTO;
import com.example.project1.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface MapUserUpdate {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UserUpdateDTO userUpdateDTO, @MappingTarget User user);
}
