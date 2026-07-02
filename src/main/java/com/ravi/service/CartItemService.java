package com.ravi.service;

import com.ravi.entities.Cart;
import com.ravi.entities.CartItems;
import com.ravi.entities.Product;
import com.ravi.entities.User;

public interface CartItemService {
    CartItems addCartItem(
            User user,
            Product product,
            String size,
            Integer quantity
    );

    Cart findUserCart(User user);

}
