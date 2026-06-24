package com.ravi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ravi.enums.USER_ROLE;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty(access= JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;
    private String phoneNumber;
    private USER_ROLE userRole=USER_ROLE.ROLE_CUSTOMER;

    @OneToMany
    private Set<Address> addresses=new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<Coupons> usedCoupons=new HashSet<>();
}
