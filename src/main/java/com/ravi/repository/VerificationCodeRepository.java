package com.ravi.repository;


import com.ravi.entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
        VerificationCode findByEmail(String email);
        VerificationCode findByOtp(String otp);
}
