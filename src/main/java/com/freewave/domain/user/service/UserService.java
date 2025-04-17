package com.freewave.domain.user.service;

import com.freewave.domain.user.entity.User;

public interface UserService {

    User saveUser(User newUser);

    User isValidEmail(String email);

    User isValidUser(Long userId);

    void isExistsEmail(String email);

    void lockAccount(Long userId);
}
