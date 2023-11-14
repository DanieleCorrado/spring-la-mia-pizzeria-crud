package com.example.springlamiapizzeriacrud.exceptions;

public class NameUniqueException extends RuntimeException{

    public NameUniqueException(String message) {
        super(message);
    }
}
