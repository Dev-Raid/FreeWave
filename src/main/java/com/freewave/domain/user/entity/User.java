package com.freewave.domain.user.entity;

import com.freewave.domain.common.Timestamped;
import com.freewave.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private String imageUrl;
    private String bio;

    private Boolean isAccountNonLocked;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private User(String username, String password, UserRole userRole, String nickname, String imageUrl) {
        this.email = username;
        this.password = password;
        this.userRole = userRole;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.bio = "자기소개가 없습니다. 자신을 소개해보세요!";
        isAccountNonLocked = true;
    }

    private User(Long userId, UserRole userRole, String nickname) {
        id = userId;
        this.userRole = userRole;
        this.nickname = nickname;
    }

    public static User of(String username, String password, UserRole userRole, String nickname, String imageUrl) {
        return new User(username, password, userRole, nickname, imageUrl);
    }

    public static User fromToken(Long userId, UserRole userRole, String nickname) {
        return new User(userId, userRole, nickname);
    }

    public void accountLock() {
        isAccountNonLocked = false;
    }

    public void updateProfile(String nickname, String password, UserRole userRole) {
        this.nickname = nickname;
        this.password = password;
        this.userRole = userRole;
    }

    public void updateProfileImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateProfileBio(String bio) {
        this.bio = bio;
    }
}
