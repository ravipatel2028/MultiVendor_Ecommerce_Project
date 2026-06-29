package com.ravi.service;

import com.ravi.enums.USER_ROLE;
import com.ravi.response.AuthResponse;
import com.ravi.requests.SignInRequest;
import com.ravi.response.SignUpRequest;

public interface AuthService {
    String createUser(SignUpRequest signUpRequest) throws Exception;
    void sentLoginOtp(String email, USER_ROLE role) throws Exception;
    AuthResponse Login(SignInRequest signInRequest);

}
