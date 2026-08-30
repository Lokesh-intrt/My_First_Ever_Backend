package com.example.project1.exceptions;

public class EntityNotFoundException extends Exception{

    public EntityNotFoundException(String entity,Long id)
    {
        super(entity+" not found with id "+id);
    }
}
