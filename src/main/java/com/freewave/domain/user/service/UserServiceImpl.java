package com.freewave.domain.user.service;

import com.freewave.domain.common.exception.InvalidRequestException;
import com.freewave.domain.user.entity.User;
import com.freewave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
    public void isExistsEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new InvalidRequestException("Email already exists");
        }
    }
}
