package com.freewave.domain.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileResponse {

    private final Long userId;
    private final String nickname;
    private final String userRole;
    private final String imageUrl;
    private final String email;
    private final String bio;
}
