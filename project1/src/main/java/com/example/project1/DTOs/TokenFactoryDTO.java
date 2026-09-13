package com.example.project1.DTOs;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TokenFactoryDTO {

    private String accessToken;
    private String refreshToken;
}
