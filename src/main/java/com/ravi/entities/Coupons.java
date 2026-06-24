package com.ravi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;
    private double discountPercent;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private Double minimumOrderAmount;
    private Boolean couponIsActive=true;

    @ManyToMany(mappedBy="usedCoupons",  fetch = FetchType.EAGER)
    private Set<User> users=new HashSet<>();
}
