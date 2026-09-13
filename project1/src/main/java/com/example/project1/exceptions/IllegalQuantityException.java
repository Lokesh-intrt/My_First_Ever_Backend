package com.example.project1.exceptions;

public class IllegalQuantityException extends RuntimeException{

    public IllegalQuantityException(String name)
    {
        super("the amount/quantity you desire of the product "+name +" is not available");
    }
}
