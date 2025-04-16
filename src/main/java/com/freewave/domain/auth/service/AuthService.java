package com.freewave.domain.auth.service;

import com.freewave.domain.auth.dto.request.SignupRequest;
import com.freewave.domain.auth.dto.response.SignupResponse;

public interface AuthService {

    SignupResponse signup(SignupRequest signupRequest);
}
