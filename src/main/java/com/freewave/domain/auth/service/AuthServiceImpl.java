package com.freewave.domain.auth.service;

import com.freewave.domain.auth.dto.request.SignupRequest;
import com.freewave.domain.auth.dto.response.SignupResponse;
import com.freewave.domain.user.entity.User;
import com.freewave.domain.user.enums.UserRole;
import com.freewave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {
        userService.isExistsEmail(signupRequest.getEmail());

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        User newUser = User.of(signupRequest.getEmail(), encodedPassword, userRole, signupRequest.getNickname(), signupRequest.getImageUrl());
        User savedUser = userService.saveUser(newUser);

        return new SignupResponse(savedUser.getEmail());
    }
}
