package com.freewave.domain.common.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.freewave.domain.common.exception.InvalidTokenException;
import com.freewave.domain.common.exception.JwtTokenExpiredException;
import com.freewave.domain.user.entity.User;
import com.freewave.domain.user.enums.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (request.getRequestURI().startsWith("/api/v1/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenValue = jwtUtil.getAccessTokenFromHeader(request);

        if (StringUtils.hasText(tokenValue)) {
            try {
                DecodedJWT info = jwtUtil.validateToken(tokenValue);
                setAuthentication(info);
            } catch (JwtTokenExpiredException e) {
                handleUnauthorizedResponse(response, "Expired token: " + e.getMessage());
                return;
            } catch (InvalidTokenException e) {
                handleUnauthorizedResponse(response, "Token verification failed: " + e.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // 인증 처리
    private void setAuthentication(DecodedJWT info) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(info);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(DecodedJWT info) {
        User user = User.fromToken(Long.valueOf(info.getSubject()), UserRole.of(info.getClaim("role").asString()), info.getClaim("nickname").asString());
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        return new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
    }

    private void handleUnauthorizedResponse(HttpServletResponse response, String logMessage) {
        log.error(logMessage);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
