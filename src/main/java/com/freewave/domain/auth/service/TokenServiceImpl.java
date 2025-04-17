package com.freewave.domain.auth.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.freewave.domain.auth.dto.TokenPair;
import com.freewave.domain.auth.entity.RefreshToken;
import com.freewave.domain.auth.repository.RefreshTokenRepository;
import com.freewave.domain.common.exception.AnomalyDetectionException;
import com.freewave.domain.common.exception.InvalidTokenException;
import com.freewave.domain.common.security.JwtUtil;
import com.freewave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenServiceImpl implements TokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public TokenPair refreshTokens(TokenPair tokenPair) {
        DecodedJWT info = jwtUtil.getUserInfoFromToken(tokenPair.getAccessToken());
        Long userId = Long.valueOf(info.getSubject());

        RefreshToken refreshToken = refreshTokenRepository.findById(userId).orElseThrow(
                () -> new InvalidTokenException("Not found refreshToken")
        );

        if (!refreshToken.getRefreshToken().equals(tokenPair.getRefreshToken())) {
            log.info("Anomaly Detection");

            userService.lockAccount(userId);
            refreshTokenRepository.delete(refreshToken);

            throw new AnomalyDetectionException("Anomaly Detection");
        } else {
            String nickname = info.getClaim("nickname").asString();
            String role = info.getClaim("role").asString();

            String newAccessToken = jwtUtil.createAccessToken(userId, nickname, role);
            String newRefreshToken = jwtUtil.createRefreshToken(userId);

            refreshTokenRepository.save(new RefreshToken(userId, newRefreshToken));

            return new TokenPair(newAccessToken, newRefreshToken);
        }
    }
}
