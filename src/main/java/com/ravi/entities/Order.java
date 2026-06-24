package com.ravi.entities;


import com.ravi.enums.OrderStatus;
import com.ravi.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders_tbl")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long sellerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address shippingAddress;

    private Double totalMrpPrice;

    private Double totalSellingPrice;
    private Double discount;

    private OrderStatus orderStatus;

    private Integer totalItems;

    @Embedded
    private PaymentDetails paymentDetails=new PaymentDetails();


    //This line creates duplicacy as same PaymentStatus is also in PaymentDetails class.
    //private PaymentStatus paymentStatus=PaymentStatus.PENDING;

    private LocalDateTime orderDate=LocalDateTime.now();
    private LocalDateTime deliveryDate=orderDate.plusDays(7);


}
