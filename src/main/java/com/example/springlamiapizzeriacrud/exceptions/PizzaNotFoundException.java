package com.example.springlamiapizzeriacrud.exceptions;

public class PizzaNotFoundException extends RuntimeException{

    public PizzaNotFoundException(String message) {
        super(message);
    }
}
