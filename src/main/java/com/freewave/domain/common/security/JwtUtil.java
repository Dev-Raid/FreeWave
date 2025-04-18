package com.freewave.domain.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.freewave.domain.common.exception.InvalidTokenException;
import com.freewave.domain.common.exception.JwtTokenExpiredException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Configuration
public class JwtUtil {

    private final String AUTHORIZATION_HEADER = "Authorization";

    private final String REFRESH_TOKEN_HEADER = "refreshToken";

    private final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.access.secret.key}")
    private String accessSecretKey;

    @Value("${jwt.refresh.secret.key}")
    private String refreshSecretKey;

    public String createAccessToken(Long userId, String nickname, String role) {
        return BEARER_PREFIX + JWT.create()
                .withSubject(userId.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 30 * 1000L)) // 30분
                .withClaim("nickname", nickname)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(accessSecretKey));
    }

    public String createRefreshToken(Long userId) {
        return BEARER_PREFIX + JWT.create()
                .withSubject(userId.toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 24 * 14 * 1000L)) // 2주
                .sign(Algorithm.HMAC512(refreshSecretKey));
    }

    // Access Token 헤더 설정
    public void addAccessTokenToHeader(HttpServletResponse response, String accessToken) {
        response.addHeader(AUTHORIZATION_HEADER, accessToken);
    }

    // Refresh Token 쿠키 설정
    public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        refreshToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8).replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행
        Cookie cookie = new Cookie(REFRESH_TOKEN_HEADER, refreshToken);
        cookie.setHttpOnly(true);        // XSS 공격 방지를 위해 HttpOnly 설정. HttpOnly로 설정하여 JavaScript에서 접근 불가
        cookie.setMaxAge(60 * 60 * 24 * 14);  // Refresh Token 쿠키의 만료 시간 1일로 설정 (Refresh Token 만료 시간과는 별개)
        cookie.setPath("/");             // 쿠키의 경로를 루트로 설정
        cookie.setSecure(true); // 운영환경에서는 true(https)
//        cookie.setDomain("localhost"); // 실제 운영 환경에서는 setDomain을 배포된 서버의 도메인(예: "example.com")으로 설정해야 함
        cookie.setAttribute("SameSite", "None"); // 프론트와 백엔드가 도메인이 다르면 Lax 또는 None
        response.addCookie(cookie);
    }

    // header 에서 JWT 가져오기
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 쿠키에서 Refresh Token 추출
    public String resolveTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(REFRESH_TOKEN_HEADER)) {
                    // 쿠키 값 디코딩
                    return java.net.URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                }
            }
        }
        return null;  // 쿠키가 없으면 null 반환
    }

    // 토큰 검증 및 디코딩
    public DecodedJWT validateToken(String token) {
        token = token.replace(BEARER_PREFIX, ""); // Bearer 접두사 제거

        try {
            return JWT
                    .require(Algorithm.HMAC512(accessSecretKey))
                    .build()
                    .verify(token); // 검증 성공 시 DecodedJWT 반환
        } catch (TokenExpiredException e) {
            throw new JwtTokenExpiredException("Expired JWT token", e); // 예외를 던져 호출부에서 처리 가능하도록 함
        } catch (SignatureVerificationException e) {
            throw new InvalidTokenException("Invalid JWT signature");
        } catch (AlgorithmMismatchException e) {
            throw new InvalidTokenException("Unsupported JWT token");
        } catch (JWTDecodeException | IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid JWT claims");
        } catch (JWTVerificationException e) {
            throw new InvalidTokenException("JWT verification failed");
        }
    }

    public DecodedJWT getUserInfoFromToken(String token) {
        token = token.replace(BEARER_PREFIX, ""); // Bearer 접두사 제거

        try {
            return JWT.decode(token); // 만료된 토큰도 강제로 디코딩
        } catch (Exception e) {
            throw new InvalidTokenException("토큰 파싱 실패");
        }
    }

    public void clearAllCookies(HttpServletRequest request, HttpServletResponse response) {
        // 요청에서 모든 쿠키가져옴.
        Cookie[] cookies = request.getCookies();

        // 모든 쿠키에 대해 값과 만료를 설정하여 삭제.
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setValue(null);
                cookie.setMaxAge(0);  // 즉시 만료
                cookie.setHttpOnly(true);
                cookie.setPath("/");  // 모든 경로에 대해 삭제 적용
                response.addCookie(cookie);
            }
        }
    }
}