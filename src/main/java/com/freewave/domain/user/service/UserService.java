package com.freewave.domain.user.service;

import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.user.dto.request.UserProfileBioRequest;
import com.freewave.domain.user.dto.request.UserProfileRequest;
import com.freewave.domain.user.dto.response.UserFromTokenResponse;
import com.freewave.domain.user.dto.response.UserProfileImageResponse;
import com.freewave.domain.user.dto.response.UserProfileResponse;
import com.freewave.domain.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User saveUser(User newUser);

    User isValidEmail(String email);

    User isValidUser(Long userId);

    void isExistsEmail(String email);

    void lockAccount(Long userId);

    UserProfileResponse getUser(PrincipalDetails principalDetails);

    UserFromTokenResponse getUserFromToken(String token);

    UserProfileResponse updateUserProfile(PrincipalDetails principalDetails, UserProfileRequest userProfileRequest);

    UserProfileImageResponse updateUserProfileImage(PrincipalDetails principalDetails, MultipartFile file);

    UserProfileResponse updateUserProfileBio(PrincipalDetails principalDetails, UserProfileBioRequest userProfileBioRequest);
}
