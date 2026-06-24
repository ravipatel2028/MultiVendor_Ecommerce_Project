package com.ravi.service;

import com.ravi.entities.Seller;
import com.ravi.enums.AccountStatus;

import java.util.List;

public interface SellerService {
    Seller getSellerProfile(String jwtToken);
    Seller createSeller(Seller seller);
    Seller getSellerById(Long sellerId);
    Seller updateSeller(Long Id, Seller seller);
    Seller getSellerByEmail(String email);
    List<Seller> getAllSellers(AccountStatus accountStatus);
    void deleteSeller(Long sellerId);
    Seller verifySeller(String email, String otp);
    Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus);
}
