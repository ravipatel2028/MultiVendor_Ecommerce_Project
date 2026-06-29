package com.ravi.service.impl;

import com.ravi.config.JwtProvider;
import com.ravi.entities.Cart;
import com.ravi.entities.Seller;
import com.ravi.entities.User;
import com.ravi.entities.VerificationCode;
import com.ravi.enums.USER_ROLE;
import com.ravi.repository.CartRepository;
import com.ravi.repository.SellerRepository;
import com.ravi.repository.UserRepository;
import com.ravi.repository.VerificationCodeRepository;
import com.ravi.response.AuthResponse;
import com.ravi.requests.SignInRequest;
import com.ravi.response.SignUpRequest;
import com.ravi.service.AuthService;
import com.ravi.service.EmailService;
import com.ravi.utils.OtpUtils;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomUserServiceImpl  customUserServiceImpl;
    private final SellerRepository sellerRepository;

    @Override
    @Transactional
    public String createUser(SignUpRequest signUpRequest) throws Exception {

        VerificationCode verificationCode = verificationCodeRepository.findByEmail(signUpRequest.getEmail());

        if (verificationCode == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No OTP found for this email");
        }

        if (!verificationCode.getOtp().equals(signUpRequest.getOtp())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new EntityExistsException("Email already exists");
        }
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setFullName(signUpRequest.getFullName());
        user.setUserRole(USER_ROLE.ROLE_CUSTOMER);
        user.setPhoneNumber("9852336306");
        User savedUser=userRepository.save(user);

        //Creating Cart for User
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);



        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(savedUser.getUserRole().name())); //Every Java enum automatically gets a name() method and it converts UserRole to String which is expected by the GrantedAuthority class constructor.

        Authentication authentication = new UsernamePasswordAuthenticationToken(signUpRequest.getEmail(), null, grantedAuthorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtProvider.generateToken(authentication);
    }

    @Override
    @Transactional
    public void sentLoginOtp(String email, USER_ROLE role) throws Exception {

        String otp = OtpUtils.generateOtpCode();

        String SIGNING_PREFIX = "signing_";
        String SELLER_PREFIX="seller_";

        if(email.startsWith(SIGNING_PREFIX)) {

            email = email.substring(
                    SIGNING_PREFIX.length());
            if(role == USER_ROLE.ROLE_CUSTOMER) {

                userRepository.findByEmail(email)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "User not found"));
            }else if(role == USER_ROLE.ROLE_SELLER) {
                Seller seller=sellerRepository.findBySellerEmail(email);
                if(seller == null) {
                    throw new EntityNotFoundException("Seller with email " + email + " not found");
                }
            }

        }

        VerificationCode existing =
                verificationCodeRepository.findByEmail(email);

        if(existing != null) {
            existing.setOtp(otp);
            verificationCodeRepository.save(existing);
        }
        else {
            VerificationCode verificationCode =
                    new VerificationCode();

            verificationCode.setEmail(email);
            verificationCode.setOtp(otp);
            verificationCode.setCreatedAt(
                    LocalDateTime.now());

            verificationCodeRepository.saveAndFlush(
                    verificationCode);
        }
        emailService.sendVerificationOtpEmail(
                email,
                otp,
                "Ecomm Multivendor OTP",
                "Your OTP is: "
        );
    }

    @Override
    public AuthResponse Login(SignInRequest signInRequest) {
        String email=signInRequest.getEmail();
        String otp=signInRequest.getOtp();
        Authentication authentication = authenticate(email, otp);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=jwtProvider.generateToken(authentication);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(token);
        authResponse.setMessage("Login Successful.");


        Collection<? extends GrantedAuthority> grantedAuthorities =
                authentication.getAuthorities();
        String userRole=grantedAuthorities
                .iterator()
                .next()
                .getAuthority();

        authResponse.setUserRole(USER_ROLE.valueOf(userRole));

        return authResponse;
    }

    private Authentication authenticate(String username, String otp) {

        UserDetails userDetails = customUserServiceImpl.loadUserByUsername(username);
        String SELLER_PREFIX="seller_";
        if(username.startsWith(SELLER_PREFIX)) {
            username = username.substring(SELLER_PREFIX.length());

        }
        if(userDetails == null) {
            throw new EntityNotFoundException("Bad Credentials(Username or Email entered is Wrong). Please try again");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
        if(verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new EntityNotFoundException("Wrong OTP");
        }
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
    }
}

