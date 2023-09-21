package com.example.emarket.exceptions;

public class TokenExpiredException extends Exception {
    @Override
    public String getMessage() {
        return "Token expired";
    }
}
