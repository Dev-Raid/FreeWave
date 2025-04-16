package com.freewave.domain.common.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.freewave.domain.common.exception.InvalidRequestException;
import com.freewave.domain.common.exception.JwtTokenExpiredException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

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

    private final long ACCESS_TOKEN_EXPIRATION = 60 * 30 * 1000L; // 30분
    private final long REFRESH_TOKEN_EXPIRATION = 60 * 60 * 24 * 14 * 1000L; // 2주

    public String createAccessToken(PrincipalDetails principalDetails) {

        return BEARER_PREFIX + JWT.create()
                .withSubject(principalDetails.getUser().getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .withClaim("role", principalDetails.getUser().getUserRole().getAuthority())
                .sign(Algorithm.HMAC512(accessSecretKey));
    }

    public String createRefreshToken(PrincipalDetails principalDetails) {

        return BEARER_PREFIX + JWT.create()
                .withSubject(principalDetails.getUser().getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .withClaim("role", principalDetails.getUser().getUserRole().getAuthority())
                .sign(Algorithm.HMAC512(refreshSecretKey));
    }

    // Access Token을 쿠키에 저장 (Bearer prefix 없이)
    public void addAccessTokenToCookie(HttpServletResponse response, String token) {
        token = URLEncoder.encode(token, StandardCharsets.UTF_8).replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

        Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token);
        cookie.setHttpOnly(true);  // XSS 공격 방지를 위해 HttpOnly 설정. HttpOnly로 설정하여 JavaScript에서 접근 불가
        cookie.setMaxAge(24 * 60 * 60);  // Access Token 쿠키의 만료 시간 1일로 설정 (Access Token 만료 시간과는 별개)
        cookie.setPath("/");  // 쿠키의 경로를 루트로 설정
        cookie.setSecure(false);
        cookie.setDomain("localhost"); // 실제 운영 환경에서는 setDomain을 배포된 서버의 도메인(예: "example.com")으로 설정해야 함
        cookie.setAttribute("SameSite", "Strict"); // 크로스 도메인 요청 허용
        response.addCookie(cookie);
    }

    // Refresh Token 쿠키 설정
    public void addRefreshTokenToCookie(HttpServletResponse response, String refreshToken) {
        refreshToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8).replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행
        Cookie cookie = new Cookie(REFRESH_TOKEN_HEADER, refreshToken);
        cookie.setHttpOnly(true);        // XSS 공격 방지를 위해 HttpOnly 설정. HttpOnly로 설정하여 JavaScript에서 접근 불가
        cookie.setMaxAge(24 * 60 * 60);  // Refresh Token 쿠키의 만료 시간 1일로 설정 (Refresh Token 만료 시간과는 별개)
        cookie.setPath("/");             // 쿠키의 경로를 루트로 설정
        cookie.setSecure(false);
        cookie.setDomain("localhost"); // 실제 운영 환경에서는 setDomain을 배포된 서버의 도메인(예: "example.com")으로 설정해야 함
        cookie.setAttribute("SameSite", "Strict"); // 크로스 도메인 요청 허용
        response.addCookie(cookie);
    }

    // 쿠키에서 Access Token 추출
    public String resolveTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
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
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
            throw new JwtTokenExpiredException("Expired JWT token", e); // 예외를 던져 호출부에서 처리 가능하도록 함
        } catch (SignatureVerificationException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
            throw new InvalidRequestException("Invalid JWT signature");
        } catch (AlgorithmMismatchException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
            throw new InvalidRequestException("Unsupported JWT token");
        } catch (JWTDecodeException | IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
            throw new InvalidRequestException("Invalid JWT claims");
        } catch (JWTVerificationException e) {
            log.error("JWT verification failed, 일반적인 검증 오류");
            throw new InvalidRequestException("JWT verification failed");
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