package com.ravi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    @ManyToMany
    @JoinTable(
            name="wishlist_products",
            joinColumns = @JoinColumn(name="wishlist_id"),
            inverseJoinColumns = @JoinColumn(name="product_id")
    )
    //avoid cascading here as deleting wishlist should not delete the product.
    private Set<Product> products=  new HashSet<>();
}
