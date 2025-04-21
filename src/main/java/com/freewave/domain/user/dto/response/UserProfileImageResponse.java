package com.freewave.domain.user.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileImageResponse {
    
    private final String imageUrl;
}
