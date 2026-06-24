package com.ravi.service.impl;

import com.ravi.entities.Seller;
import com.ravi.entities.User;
import com.ravi.enums.USER_ROLE;
import com.ravi.repository.SellerRepository;
import com.ravi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private static final String SELLER_PREFIX="seller_";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(username.startsWith(SELLER_PREFIX)) {
            String actualUsername = username.substring(SELLER_PREFIX.length());
            Seller seller=sellerRepository.findBySellerEmail(actualUsername);
            if(seller != null) {
                return buildUserDetails(seller.getSellerEmail(), seller.getPassword(), seller.getRole());
            }
        }else{
            User user = userRepository.findByEmail(username)
                    .orElseThrow(()->new UsernameNotFoundException("Username : "+ username +" not found"));
            if(user != null) {
                return buildUserDetails(user.getEmail(), user.getPassword(), user.getUserRole());
            }
        }
        throw new UsernameNotFoundException("User or Seller not found with name : "+username);
    }

    private UserDetails buildUserDetails(String email, String password, USER_ROLE userRole) {
        if(userRole == null)
            userRole=USER_ROLE.ROLE_CUSTOMER;
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userRole.toString()));

        return new org.springframework.security.core.userdetails.User(email, password, authorities);
    }
}
