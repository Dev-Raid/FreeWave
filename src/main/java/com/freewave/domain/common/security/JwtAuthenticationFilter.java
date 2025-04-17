package com.freewave.domain.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freewave.domain.auth.dto.request.LoginRequest;
import com.freewave.domain.auth.entity.RefreshToken;
import com.freewave.domain.auth.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    // login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        log.info("로그인 성공 및 JWT 생성");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        Long userId = principalDetails.getUser().getId();
        String nickname = principalDetails.getUser().getNickname();
        String role = principalDetails.getUser().getUserRole().getAuthority();

        String accessToken = jwtUtil.createAccessToken(userId, nickname, role);
        String refreshToken = jwtUtil.createRefreshToken(userId);

        tokenService.saveRefreshToken(new RefreshToken(userId, refreshToken));

        jwtUtil.addAccessTokenToHeader(response, accessToken);
        jwtUtil.addRefreshTokenToCookie(response, refreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        super.unsuccessfulAuthentication(request, response, failed);
        response.setStatus(401);
    }
}
