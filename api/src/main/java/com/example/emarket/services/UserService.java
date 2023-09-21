package com.example.emarket.services;

import com.example.emarket.enums.Role;
import com.example.emarket.exceptions.InvalidCredentialsException;
import com.example.emarket.exceptions.UserAlreadyExistsException;
import com.example.emarket.exceptions.UserDoesNotExistException;
import com.example.emarket.models.entities.User;
import com.example.emarket.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User registerUser(String email, String password) throws UserAlreadyExistsException {
        User userFetched = userRepository.findByEmail(email);

        if (userFetched != null) {
            throw new UserAlreadyExistsException();
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(email)
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    public User login(String email, String password) throws UserDoesNotExistException, InvalidCredentialsException {

        User userFetched = userRepository.findByEmail(email);

        if (userFetched == null) {
            throw new UserDoesNotExistException();
        }

        if (!passwordEncoder.matches(password, userFetched.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return userFetched;
    }
}

