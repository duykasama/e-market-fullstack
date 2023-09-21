package com.example.emarket.exceptions;

public class UserAlreadyExistsException extends Exception {
    @Override
    public String getMessage() {
        return "User already exists";
    }
}
