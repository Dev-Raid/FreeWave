package com.freewave.domain.auth.controller;

import com.freewave.domain.auth.dto.TokenPair;
import com.freewave.domain.auth.dto.request.SignupRequest;
import com.freewave.domain.auth.dto.response.SignupResponse;
import com.freewave.domain.auth.service.AuthService;
import com.freewave.domain.auth.service.TokenService;
import com.freewave.domain.common.component.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    @PostMapping("/v1/auth/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {

        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signupRequest));
    }

    @PostMapping("/v1/auth/refresh")
    public ResponseEntity<TokenPair> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);
        String refreshToken = jwtUtil.resolveTokenFromCookie(request);

        TokenPair oldTokens = new TokenPair(accessToken, refreshToken);
        TokenPair newTokens = tokenService.refreshTokens(oldTokens);

        jwtUtil.addAccessTokenToHeader(response, newTokens.getAccessToken());
        jwtUtil.addRefreshTokenToCookie(response, newTokens.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED).body(newTokens);
    }
}
