package com.freewave.domain.auth.controller;

import com.freewave.domain.auth.dto.request.SignupRequest;
import com.freewave.domain.auth.dto.response.SignupResponse;
import com.freewave.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/auth/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signupRequest));
    }
}
