package com.example.project1.exceptions;

import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> dataBaseExceptions(DataIntegrityViolationException e)
    {
        String rootMsg = e.getRootCause() != null ? e.getRootCause().getMessage() : e.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = "data is not valid";

        if((rootMsg != null)&&(rootMsg.contains("Duplicate entry") || rootMsg.contains("unique constraint")))
        {
            if(rootMsg.contains("email"))
                msg = "email is already registered";
            else if (rootMsg.contains("name")) {
                msg = "Name is already taken. Please try another name.";
            }
            status = HttpStatus.CONFLICT;
        }

        ExceptionResponse exceptionResponse = ExceptionResponse.builder().msg(msg).build();
        return ResponseEntity.status(status).body(exceptionResponse);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> fieldExceptions(MethodArgumentNotValidException e)
    {
        String msg = "Validation failed";
        HashMap<String,String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(),error.getDefaultMessage()));

        ExceptionResponse exceptionResponse = new ExceptionResponse(msg , errors);

        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> userNotFoundHandling(EntityNotFoundException e)
    {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> invalidCredentials(BadCredentialsException e)
    {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials. Please enter valid credentials");
    }


    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<String> jwtTokenFail(JWTCreationException e)
    {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("could not authorise. try later");
    }

}
