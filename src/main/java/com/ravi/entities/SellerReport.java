package com.ravi.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="seller_id")
    private Seller seller;


    private Double totalEarnings;
    private Double totalSales;
    private Double totalRefunds;
    private Double totalTax;
    private Double totalNetEarnings;

    private Double totalOrders;
    private Double cancelledOrders;
    private  Integer totalTransactions;
}
