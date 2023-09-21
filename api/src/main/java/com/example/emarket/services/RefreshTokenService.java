package com.example.emarket.services;

import com.example.emarket.models.entities.RefreshToken;
import com.example.emarket.repositories.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public boolean exists(String token) {
        return refreshTokenRepository.existsByToken(token);
    }

    public RefreshToken saveToken(String token) {
        return refreshTokenRepository.save(
                    RefreshToken.builder()
                            .token(token)
                            .build()
        );
    }
}
