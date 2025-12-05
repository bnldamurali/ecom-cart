package com.ecom.cart.service;

import com.ecom.cart.dto.AddToCartRequest;
import com.ecom.cart.dto.UpdateCartRequest;
import com.ecom.cart.entity.EcomCart;
import com.ecom.cart.repository.EcomCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EcomCartServiceImpl implements EcomCartService {

    private final EcomCartRepository ecomCartRepository;

    @Override
    public EcomCart addToCart(AddToCartRequest addToCartRequest) {
        EcomCart cart = EcomCart.builder()
                .userId(addToCartRequest.getUserId())
                .productId(addToCartRequest.getProductId())
                .quantity(addToCartRequest.getQuantity())
                .build();

        return ecomCartRepository.save(cart);
    }

    @Override
    public EcomCart updateCartProduct(Long cartItemId, UpdateCartRequest updateCartRequest) {
        EcomCart cartItem = ecomCartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart product not found"));

        cartItem.setQuantity(updateCartRequest.getQuantity());
        return ecomCartRepository.save(cartItem);
    }

    @Override
    public void deleteCartProduct(Long cartItemId) {
        if (!ecomCartRepository.existsById(cartItemId)) {
            throw new RuntimeException("Cart product not found");
        }
        ecomCartRepository.deleteById(cartItemId);
    }
}
