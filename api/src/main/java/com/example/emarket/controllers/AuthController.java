package com.example.emarket.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @PostMapping
    public String getAuthToken(String email, String password) {
        if (email.equals("duykasama@gmail.com") && password.equals("123")) {
            return "authenticated";
        }
        return "unauthenticated";
    }
}
