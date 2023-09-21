package com.example.emarket.exceptions;

public class InvalidCredentialsException extends Exception{
    @Override
    public String getMessage() {
        return "Wrong username or password";
    }
}
