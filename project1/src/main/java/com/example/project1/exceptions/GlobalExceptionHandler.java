package com.example.project1.exceptions;

import com.auth0.jwt.exceptions.JWTCreationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler
{
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleAllExceptions(Exception ex, HttpServletRequest request)
    {
        Map<String,Object> body = new HashMap<>();

        // 1. Force the exact Java Exception Name to print out
        body.put("exception", ex.getClass().getSimpleName());

        // 2. Extract the exact reason line
        body.put("message", ex.getMessage());
        body.put("path", request.getRequestURI());

        // Print the stack trace in your IDE terminal so you don't even need Postman to show it
        ex.printStackTrace();

        // Return a clean 500 Internal Server Error back to Postman, bypassing the /error route
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
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

        ExceptionResponse exceptionResponse = ExceptionResponse.builder().msg(msg).errors(errors).build();

        return ResponseEntity.badRequest().body(exceptionResponse);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> userNotFoundHandling(ResourceNotFoundException e)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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

    @ExceptionHandler(IllegalQuantityException.class)
    public ResponseEntity<String> illegalQuantity(IllegalQuantityException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ExceptionResponse> optimisticLockFailure(ObjectOptimisticLockingFailureException e, HttpServletRequest request)
    {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ExceptionResponse.builder().
                msg("\"The Item You are trying to interact with has been modified. Please try again!\"")
                .path(request.getRequestURI()).build());
    }

}
