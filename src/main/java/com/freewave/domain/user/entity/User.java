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

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private User(String username, String password, UserRole userRole, String nickname) {
        this.email = username;
        this.password = password;
        this.userRole = userRole;
        this.nickname = nickname;
    }

    public static User of(String username, String password, UserRole userRole, String nickname) {
        return new User(username, password, userRole, nickname);
    }
}
