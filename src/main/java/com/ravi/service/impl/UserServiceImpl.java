package com.ravi.service.impl;

import com.ravi.config.JwtProvider;
import com.ravi.entities.User;
import com.ravi.repository.UserRepository;
import com.ravi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;


    @Override
    public User findUserByEmail(String email) {

        User user=userRepository.findByEmail(email)
                .orElseThrow(()->new EntityNotFoundException("User not found with email - "+ email));
        return user;
    }

    @Override
    public User findUserByJwtToken(String jwtToken) {
        String email=jwtProvider.getEmailFromToken(jwtToken);
        User user=this.findUserByEmail(email);
        if(user==null){
            throw new EntityNotFoundException("User not found with given email - "+ email);
        }
        return user;
    }
}
