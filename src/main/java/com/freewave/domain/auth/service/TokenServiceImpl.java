package com.freewave.domain.auth.service;

import com.freewave.domain.auth.dto.TokenPair;
import com.freewave.domain.auth.entity.RefreshToken;
import com.freewave.domain.auth.exception.AnomalyDetectionException;
import com.freewave.domain.auth.repository.RefreshTokenRepository;
import com.freewave.domain.common.component.JwtUtil;
import com.freewave.domain.common.exception.InvalidTokenException;
import com.freewave.domain.user.dto.response.UserFromTokenResponse;
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
        UserFromTokenResponse user = userService.getUserFromToken(tokenPair.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findById(user.getUserId()).orElseThrow(
                () -> new InvalidTokenException("Not found refreshToken")
        );

        if (!refreshToken.getRefreshToken().equals(tokenPair.getRefreshToken())) {
            log.info("Anomaly Detection");

            userService.lockAccount(user.getUserId());
            refreshTokenRepository.delete(refreshToken);

            throw new AnomalyDetectionException("Anomaly Detection");
        } else {
            String newAccessToken = jwtUtil.createAccessToken(user.getUserId(), user.getNickname(), user.getUserRole());
            String newRefreshToken = jwtUtil.createRefreshToken(user.getUserId());

            refreshTokenRepository.save(new RefreshToken(user.getUserId(), newRefreshToken));

            return new TokenPair(newAccessToken, newRefreshToken);
        }
    }
}
