package com.ravi.service.impl;

import com.ravi.entities.Cart;
import com.ravi.entities.CartItems;
import com.ravi.entities.Product;
import com.ravi.entities.User;
import com.ravi.repository.CartItemRepository;
import com.ravi.repository.CartRepository;
import com.ravi.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CartItemsServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Override
    public CartItems addCartItem(User user, Product product, String size, Integer quantity) {
        return null;
    }

    @Override
    public Cart findUserCart(User user) {

        Cart cart = cartRepository.findByUserId(user.getId());

        Double totalPrice = 0.0;
        Double totalDiscountPrice = 0.0;
        Integer totalItem = 0;

        for(CartItems cartItems : cart.getCartItemsSet()){
            totalPrice +=cartItems.getPrice();
            totalDiscountPrice += cartItems.getSellingPrice();
            totalItem += cartItems.getQuantity();
        }

        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItems(totalItem);
        cart.setTotalSellingPrice(totalDiscountPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice, totalDiscountPrice));

        return null;
    }

    private Double calculateDiscountPercentage(Double mrpPrice, Double sellingPrice) {
        return null;
    }
}
