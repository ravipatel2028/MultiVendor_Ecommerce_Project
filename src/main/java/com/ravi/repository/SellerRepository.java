package com.ravi.repository;

import com.ravi.entities.Seller;
import com.ravi.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findBySellerName(String name);
    Seller findBySellerEmail(String email);
    List<Seller> findByAccountStatus(AccountStatus accountStatus);

}
