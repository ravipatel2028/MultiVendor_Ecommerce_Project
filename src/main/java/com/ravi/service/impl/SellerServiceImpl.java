package com.ravi.service.impl;

import com.ravi.config.JwtProvider;
import com.ravi.entities.Address;
import com.ravi.entities.Seller;
import com.ravi.entities.VerificationCode;
import com.ravi.enums.AccountStatus;
import com.ravi.enums.USER_ROLE;
import com.ravi.repository.AddressRepository;
import com.ravi.repository.SellerRepository;
import com.ravi.repository.VerificationCodeRepository;
import com.ravi.service.SellerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider  jwtProvider;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationCodeRepository verificationCodeRepository;

    @Override
    public Seller getSellerProfile(String jwtToken) {
        String email=jwtProvider.getEmailFromToken(jwtToken);

        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) {

        Seller sellerExists=sellerRepository.findBySellerEmail(seller.getSellerEmail());
        if(sellerExists!=null) {
            throw new EntityExistsException("Seller with email "+seller.getSellerEmail()+" already exists");
        }

        Address address=addressRepository.save(seller.getPickUpAddress());

        Seller newSeller=new Seller();
        newSeller.setSellerEmail(seller.getSellerEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setSellerPhoneNumber(seller.getSellerPhoneNumber());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setBusinessDetails(seller.getBusinessDetails());
        newSeller.setGstId(seller.getGstId());
        newSeller.setPickUpAddress(address);
        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(()->new EntityNotFoundException("Seller with id "+sellerId+" not found"));
    }

    @Override
    public Seller updateSeller(Long Id, Seller seller) {
        Seller sellerExists=sellerRepository.findById(Id)
                .orElseThrow(()->new EntityNotFoundException("Seller with id "+Id+" not found"));

        if(seller.getSellerName() != null) {
            sellerExists.setSellerName(seller.getSellerName());
        }
        if(seller.getSellerPhoneNumber() != null) {
            sellerExists.setSellerPhoneNumber(seller.getSellerPhoneNumber());
        }
        if(seller.getBankDetails() != null
            && seller.getBankDetails().getAccountNumber() != null
            && seller.getBankDetails().getIfscCode() != null
            && seller.getBankDetails().getAccountHolderNumber() != null) {

            sellerExists.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
            sellerExists.getBankDetails().setAccountHolderNumber(seller.getBankDetails().getAccountHolderNumber());
            sellerExists.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
        }
        if(seller.getBusinessDetails() != null) {
            sellerExists.setBusinessDetails(seller.getBusinessDetails());
        }
        if(seller.getGstId() != null) {
            sellerExists.setGstId(seller.getGstId());
        }
        if(seller.getPickUpAddress() != null
            && seller.getPickUpAddress().getArea() != null
                && seller.getPickUpAddress().getCity() != null
                && seller.getPickUpAddress().getCountry() != null
                && seller.getPickUpAddress().getDistrict() != null
        ) {
            sellerExists.getPickUpAddress().setArea(seller.getPickUpAddress().getArea());
            sellerExists.getPickUpAddress().setCity(seller.getPickUpAddress().getCity());
            sellerExists.getPickUpAddress().setCountry(seller.getPickUpAddress().getCountry());
            sellerExists.getPickUpAddress().setDistrict(seller.getPickUpAddress().getDistrict());
        }
        if(sellerExists.getSellerEmail() != null) {
            sellerExists.setSellerEmail(sellerExists.getSellerEmail());
        }
        return sellerRepository.save(sellerExists);
    }

    @Override
    public Seller getSellerByEmail(String email) {
        Seller seller=sellerRepository.findBySellerEmail(email);
        if(seller==null){
            throw new EntityNotFoundException("Seller with email "+email+" not found");
        }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus accountStatus) {

        return sellerRepository.findByAccountStatus(accountStatus);
    }

    @Override
    public void deleteSeller(Long sellerId) {
        Seller seller=sellerRepository.findById(sellerId)
                .orElseThrow(()->new EntityNotFoundException("Seller with id "+sellerId+" not found"));

        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifySeller(String email, String otp) {
        VerificationCode verificationCode=verificationCodeRepository.findByEmail(email);
        if(verificationCode==null){
            throw new EntityNotFoundException("No OTP found  for this email-  "+email);
        }
        if(!verificationCode.getOtp().equals(otp)){
            throw new EntityNotFoundException("OTP does not match");
        }
        if(verificationCode.getCreatedAt().isBefore(LocalDateTime.now().minusMinutes(5))){
            throw new RuntimeException("Otp Expired");
        }
        Seller seller=sellerRepository.findBySellerEmail(email);

        if(seller==null){
            throw new EntityNotFoundException("Seller with email "+email+" not found");
        }
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus) {
        if(accountStatus ==null){
            throw new IllegalArgumentException("AccountStatus cannot be null");
        }
        Seller seller=getSellerById(sellerId);
        if(seller==null){
            throw new EntityNotFoundException("Seller with id "+sellerId+" not found");
        }
        seller.setAccountStatus(accountStatus);
        return sellerRepository.save(seller);
    }
}
