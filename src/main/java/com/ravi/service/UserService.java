package com.ravi.service;

import com.ravi.entities.User;

public interface UserService {

    User findUserByEmail(String email);
    User findUserByJwtToken(String jwtToken);

}
