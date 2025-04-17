package com.freewave.domain.auth.repository;

import com.freewave.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String token);

    void deleteByRefreshToken(String token);
}
