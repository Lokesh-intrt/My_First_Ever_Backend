package com.example.project1.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String entity)
    {
        super(entity+" not found");
    }

    public ResourceNotFoundException(String entity,Long id)
    {
        super(entity+" not found with id "+id);
    }
}
