package com.ravi.service;

import com.ravi.entities.Seller;
import com.ravi.enums.AccountStatus;
import com.ravi.exceptions.SellerException;

import java.util.List;

public interface SellerService {
    Seller getSellerProfile(String jwtToken) throws SellerException;
    Seller createSeller(Seller seller) throws SellerException;
    Seller getSellerById(Long sellerId) throws SellerException;
    Seller updateSeller(Long Id, Seller seller) throws SellerException;
    Seller getSellerByEmail(String email) throws SellerException;
    List<Seller> getAllSellers(AccountStatus accountStatus);
    void deleteSeller(Long sellerId) throws SellerException;
    Seller verifySeller(String email, String otp) throws SellerException;
    Seller updateSellerAccountStatus(Long sellerId, AccountStatus accountStatus) throws SellerException;
}
