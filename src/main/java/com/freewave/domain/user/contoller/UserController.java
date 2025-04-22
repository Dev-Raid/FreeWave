package com.freewave.domain.user.contoller;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.user.dto.request.UserProfileBioRequest;
import com.freewave.domain.user.dto.request.UserProfileRequest;
import com.freewave.domain.user.dto.response.UserProfileImageResponse;
import com.freewave.domain.user.dto.response.UserProfileResponse;
import com.freewave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/v1/users/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(principalDetails));
    }

    @PutMapping("/v1/users/profiles")
    public ResponseEntity<UserProfileResponse> updateUserProfile(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody UserProfileRequest userProfileRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserProfile(principalDetails, userProfileRequest));
    }

    @PutMapping("/v1/users/profiles/images")
    public ResponseEntity<UserProfileImageResponse> updateUserProfileImage(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserProfileImage(principalDetails, file));
    }

    @PutMapping("/v1/users/profiles/bio")
    public ResponseEntity<UserProfileResponse> updateUserProfileBio(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody UserProfileBioRequest userProfileBioRequest
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserProfileBio(principalDetails, userProfileBioRequest));
    }
}
