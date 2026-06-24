package com.ravi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "cart",  cascade = CascadeType.ALL)
    private Set<CartItems> cartItemsSet=new HashSet<>();

    private Double totalSellingPrice;
    private Integer totalItems;

    private Double totalMrpPrice;
    private Double discount;
    private String couponCode;
}
