package com.ravi.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {
    private String email;
    private String fullName;
    private String password;  // chosen account password
    private String otp;       // one-time code for verification
}

