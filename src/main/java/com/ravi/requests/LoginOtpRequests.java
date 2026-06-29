package com.ravi.requests;

import com.ravi.enums.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginOtpRequests {
    private String email;
    private String otp;
    private USER_ROLE role;
}
