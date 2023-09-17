package com.example.emarket.exceptions;

public class ApartmentNotFoundException extends Exception{
    @Override
    public String getMessage() {
        return "Apartment not found";
    }
}
