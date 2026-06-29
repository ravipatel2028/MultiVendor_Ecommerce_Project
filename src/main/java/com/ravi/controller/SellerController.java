package com.ravi.controller;

import com.ravi.config.JwtProvider;
import com.ravi.entities.Seller;
import com.ravi.entities.VerificationCode;
import com.ravi.enums.AccountStatus;
import com.ravi.exceptions.SellerException;
import com.ravi.repository.VerificationCodeRepository;
import com.ravi.response.AuthResponse;
import com.ravi.requests.SignInRequest;
import com.ravi.service.AuthService;
import com.ravi.service.EmailService;
import com.ravi.service.SellerService;
import com.ravi.utils.OtpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sellers")
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final JwtProvider jwtProvider;
    //private final SellerReportService sellerReportService;


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> sellerLogin(@RequestBody SignInRequest signInRequest) {
        //Extracting otp and email
        String otp=signInRequest.getOtp();
        String email=signInRequest.getEmail();

        signInRequest.setEmail("seller_"+email);
        AuthResponse authResponse=authService.Login(signInRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PatchMapping("/verifyOtp/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable("otp") String otp) throws SellerException {
        VerificationCode verificationCode=verificationCodeRepository.findByOtp(otp);

        if(verificationCode==null || !verificationCode.getOtp().equals(otp)){
            throw new IllegalArgumentException("Wrong OTP.");
        }

        Seller seller=sellerService.verifySeller(verificationCode.getEmail(), otp);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }


    @PostMapping("/create")
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws SellerException {
        Seller savedSeller=sellerService.createSeller(seller);

        String otp= OtpUtils.generateOtpCode();

        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getSellerEmail());
        verificationCodeRepository.save(verificationCode);

        String subject="Multivendor Ecommerce Verification Code";
        String text="Welcome to Multivendor Ecommerce , verify your account using this link. ";

        String frontendUrl="http://localhost:3000/verify-seller/";

        emailService.sendVerificationOtpEmail(seller.getSellerEmail(), verificationCode.getOtp(), subject, text+frontendUrl);

        return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable("id") Long id) throws SellerException {
        Seller seller=sellerService.getSellerById(id);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }


    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByToken(
            @RequestHeader("Authorization")  String token
    ) throws SellerException {

        String email=jwtProvider.getEmailFromToken(token);
        Seller seller=sellerService.getSellerByEmail(email);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }


//    @GetMapping("/report")
//    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String token){
//        String email=jwtProvider.getEmailFromToken(token);
//        Seller seller=sellerService.getSellerByEmail(email);
//
//        SellerReport sellerReport=sellerReportService.getSellerReport(seller);
//        return new ResponseEntity<>(sellerReport, HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false)AccountStatus accountStatus){
        List<Seller> sellers=sellerService.getAllSellers(accountStatus);
        return new ResponseEntity<>(sellers, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Seller> updateSeller(
            @RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws SellerException {

        Seller profile=sellerService.getSellerProfile(jwt);
        Seller updatedSeller=sellerService.updateSeller(profile.getId(),  seller);
        return new ResponseEntity<>(updatedSeller, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Seller> deleteSeller(@PathVariable("id") Long id) throws SellerException {
        Seller seller=sellerService.getSellerById(id);
        if(seller==null){
            throw  new IllegalArgumentException("Seller not found.");
        }
        sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }


}
