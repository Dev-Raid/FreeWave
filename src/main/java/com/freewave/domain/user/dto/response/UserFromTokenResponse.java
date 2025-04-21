package com.freewave.domain.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserFromTokenResponse {

    private final Long userId;
    private final String nickname;
    private final String userRole;
}
