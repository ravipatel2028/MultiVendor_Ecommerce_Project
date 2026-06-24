package com.ravi.controller;


import com.ravi.entities.User;
import com.ravi.entities.VerificationCode;
import com.ravi.enums.USER_ROLE;
import com.ravi.repository.UserRepository;
import com.ravi.response.ApiResponse;
import com.ravi.response.AuthResponse;
import com.ravi.response.SignInRequest;
import com.ravi.response.SignUpRequest;
import com.ravi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signUp")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest signUpRequest) throws Exception {
        String jwtToken=authService.createUser(signUpRequest);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setToken(jwtToken);
        authResponse.setMessage("success");
        authResponse.setUserRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(authResponse);
    }


    @PostMapping("/signIn")
    public ResponseEntity<AuthResponse> loginHandler(@RequestBody SignInRequest signInRequest) throws Exception {

        AuthResponse authResponse=authService.Login(signInRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> sendOtpHandler(@RequestBody VerificationCode req) {

        authService.sentLoginOtp(req.getEmail());

        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage("success");

        return ResponseEntity.ok(apiResponse);
    }
}
