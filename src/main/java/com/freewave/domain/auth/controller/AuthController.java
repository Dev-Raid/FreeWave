package com.freewave.domain.auth.controller;

import com.freewave.domain.auth.dto.TokenPair;
import com.freewave.domain.auth.dto.request.SignupRequest;
import com.freewave.domain.auth.dto.response.SignupResponse;
import com.freewave.domain.auth.service.AuthService;
import com.freewave.domain.auth.service.TokenService;
import com.freewave.domain.common.security.JwtUtil;
import com.freewave.domain.common.security.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/v1/test")
    public void test(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println(principalDetails.getUser().getId());
        System.out.println(principalDetails.getUser().getUserRole());
        System.out.println(principalDetails.getUser().getNickname());
    }
}
