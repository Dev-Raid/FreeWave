package com.freewave.domain.common.security;

import com.freewave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public PrincipalDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new PrincipalDetails(userService.isValidEmail(email));
    }
}
