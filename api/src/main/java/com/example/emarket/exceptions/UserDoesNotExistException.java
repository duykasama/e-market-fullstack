package com.example.emarket.exceptions;

public class UserDoesNotExistException extends Exception{
    @Override
    public String getMessage() {
        return "User does not exist";
    }
}
