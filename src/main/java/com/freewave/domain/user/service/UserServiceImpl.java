package com.freewave.domain.user.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.freewave.domain.common.component.JwtUtil;
import com.freewave.domain.common.exception.InvalidRequestException;
import com.freewave.domain.common.exception.UnsupportedFileTypeException;
import com.freewave.domain.common.security.PrincipalDetails;
import com.freewave.domain.common.service.S3Service;
import com.freewave.domain.common.service.S3ServiceFactory;
import com.freewave.domain.user.dto.request.UserProfileBioRequest;
import com.freewave.domain.user.dto.request.UserProfileRequest;
import com.freewave.domain.user.dto.response.UserFromTokenResponse;
import com.freewave.domain.user.dto.response.UserProfileImageResponse;
import com.freewave.domain.user.dto.response.UserProfileResponse;
import com.freewave.domain.user.entity.User;
import com.freewave.domain.user.enums.UserRole;
import com.freewave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3ServiceFactory s3ServiceFactory;
    private final JwtUtil jwtUtil;

    @Override
    public User saveUser(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public User isValidEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new InvalidRequestException("Not found user")
        );
    }

    @Override
    public User isValidUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new InvalidRequestException("Not found user")
        );
    }

    @Override
    public void isExistsEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new InvalidRequestException("Email already exists");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void lockAccount(Long userId) {
        User user = isValidUser(userId);
        user.accountLock();
    }

    @Override
    public UserProfileResponse getUser(PrincipalDetails principalDetails) {
        User user = isValidUser(principalDetails.getUser().getId());

        return new UserProfileResponse(
                user.getId(),
                user.getNickname(),
                user.getUserRole().getAuthority(),
                user.getImageUrl(),
                user.getEmail(),
                user.getBio()
        );
    }

    @Override
    public UserFromTokenResponse getUserFromToken(String token) {
        DecodedJWT info = jwtUtil.validateToken(token);

        return new UserFromTokenResponse(
                Long.valueOf(info.getSubject()),
                info.getClaim("nickname").asString(),
                info.getClaim("role").asString()
        );
    }

    @Override
    @Transactional
    public UserProfileResponse updateUserProfile(PrincipalDetails principalDetails, UserProfileRequest userProfileRequest) {
        User user = isValidUser(principalDetails.getUser().getId());

        String newPassword;
        if (userProfileRequest.getPassword().isEmpty()) {
            newPassword = user.getPassword();
        } else {
            newPassword = passwordEncoder.encode(userProfileRequest.getPassword());
        }

        user.updateProfile(userProfileRequest.getNickname(), newPassword, UserRole.of(userProfileRequest.getUserRole()));

        return new UserProfileResponse(
                user.getId(),
                user.getNickname(),
                user.getUserRole().getAuthority(),
                user.getImageUrl(),
                user.getEmail(),
                user.getBio()
        );
    }

    @Override
    @Transactional
    public UserProfileImageResponse updateUserProfileImage(PrincipalDetails principalDetails, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new InvalidRequestException("Profile image file is required");
        }

        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new UnsupportedFileTypeException("Only image files can be uploaded. Current file type: " + contentType);
        }

        User user = isValidUser(principalDetails.getUser().getId());
        S3Service s3Service = s3ServiceFactory.getServiceForContentType(file.getContentType());

        if (user.getImageUrl() != null) {
            s3Service.deleteFile(user.getImageUrl());
        }

        String imageUrl = s3Service.uploadFile(file);
        user.updateProfileImage(imageUrl);

        return new UserProfileImageResponse(user.getImageUrl());
    }

    @Override
    @Transactional
    public UserProfileResponse updateUserProfileBio(PrincipalDetails principalDetails, UserProfileBioRequest userProfileBioRequest) {
        User user = isValidUser(principalDetails.getUser().getId());

        user.updateProfileBio(userProfileBioRequest.getBio());

        return new UserProfileResponse(
                user.getId(),
                user.getNickname(),
                user.getUserRole().getAuthority(),
                user.getImageUrl(),
                user.getEmail(),
                user.getBio()
        );
    }
}
