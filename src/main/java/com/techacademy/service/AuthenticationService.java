package com.techacademy.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.techacademy.entity.Authentication;
import com.techacademy.repository.AuthenticationRepository;

@Service
public class AuthenticationService {
    private final AuthenticationRepository authenticationRepository;

    public AuthenticationService(AuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    // 1件を検索して返す（登録）
    public Authentication getAuthentication(String code) {
        // findByIdで検索
        Optional<Authentication> option = authenticationRepository.findById(code);
        // 取得できなかった場合はnullを返す
        Authentication authentication = option.orElse(null);
        return authentication;
    }

}