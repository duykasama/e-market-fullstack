package com.example.emarket.exceptions;

public class ContractNotFoundException extends Exception{
    @Override
    public String getMessage() {
        return "Contract not found";
    }
}
