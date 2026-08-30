package com.example.project1.exceptions;


import lombok.*;

import java.util.HashMap;

@Data
@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor
public class ExceptionResponse {
   private String msg ;
   private HashMap<String,String> errors;


}

