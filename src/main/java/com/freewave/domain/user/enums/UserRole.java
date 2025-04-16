package com.freewave.domain.user.enums;

import com.freewave.domain.common.exception.InvalidRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    ROLE_ADMIN(Authority.ADMIN),
    ROLE_CLIENT(Authority.CLIENT),
    ROLE_FREELANCER(Authority.FREELANCER);

    private final String authority;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("Invalid UerRole"));
    }

    public static class Authority {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String CLIENT = "ROLE_CLIENT";
        public static final String FREELANCER = "ROLE_FREELANCER";
    }
}