package com.freewave.domain.auth.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "refresh_token", timeToLive = 14 * 24 * 60 * 60)
public class RefreshToken {

    @Id
    private Long userId;

    private String refreshToken;

    public RefreshToken(Long userId, String refreshToken) {
        this.userId = userId;
        this.refreshToken = refreshToken;
    }
}
