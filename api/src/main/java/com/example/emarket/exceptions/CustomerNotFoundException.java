package com.example.emarket.exceptions;

public class CustomerNotFoundException extends Exception{
    @Override
    public String getMessage() {
        return "Customer not found";
    }
}
