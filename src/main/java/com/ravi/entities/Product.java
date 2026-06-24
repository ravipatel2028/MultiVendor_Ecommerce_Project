package com.ravi.entities;


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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String sizes;
    private String color;

    private Double sellingPrice;
    private Double mrpPrice;
    private Double discountPercentage;

    private Integer quantity;
    private Integer numRatings;


    @ElementCollection
    private List<String> images=new ArrayList<>();

    @ManyToOne
    private Category category;

    @ManyToOne
    private Seller seller;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "product",  cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Reviews> reviews=new ArrayList<>();
}
