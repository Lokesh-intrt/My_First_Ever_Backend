package com.example.project1.DTOs;

import jakarta.validation.constraints.Email;import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class UserRegisterDTO {

    @NotBlank(message = "user name cannot be blank.")
    private  String name;

    @NotBlank(message = "email cannot be blank.")
    @NotNull(message = "email cannot be null.")
    @Email
    private  String email;

    @NotBlank(message = "email cannot be blank.")
    @NotNull(message = "email cannot be null.")
    private  String password;
}
