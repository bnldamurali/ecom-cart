package com.ecom.cart.service;

import com.ecom.cart.dto.AddToCartRequest;
import com.ecom.cart.dto.UpdateCartRequest;
import com.ecom.cart.entity.EcomCart;

public interface EcomCartService {

    EcomCart addToCart(AddToCartRequest addToCartRequest);

    EcomCart updateCartProduct(Long cartItemId, UpdateCartRequest updateCartRequest);

    void deleteCartProduct(Long productId);
}
