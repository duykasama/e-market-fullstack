package com.example.emarket.exceptions;

public class FileFormatNotSupported extends Exception{
    @Override
    public String getMessage() {
        return "File format is not supported";
    }
}
