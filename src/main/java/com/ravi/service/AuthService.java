package com.ravi.service;

import com.ravi.response.AuthResponse;
import com.ravi.response.SignInRequest;
import com.ravi.response.SignUpRequest;

public interface AuthService {
    String createUser(SignUpRequest signUpRequest) throws Exception;
    void sentLoginOtp(String email);
    AuthResponse Login(SignInRequest signInRequest);

}
