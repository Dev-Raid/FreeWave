package com.freewave.domain.auth.service;

import com.freewave.domain.auth.dto.TokenPair;
import com.freewave.domain.auth.entity.RefreshToken;

public interface TokenService {

    void saveRefreshToken(RefreshToken refreshToken);

    TokenPair refreshTokens(TokenPair tokenPair);
}
