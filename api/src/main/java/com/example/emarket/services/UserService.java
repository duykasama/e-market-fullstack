package com.example.emarket.services;

import com.example.emarket.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(){}

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
