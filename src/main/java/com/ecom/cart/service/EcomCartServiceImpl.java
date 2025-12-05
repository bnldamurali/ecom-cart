package com.ecom.cart.service;

import com.ecom.cart.dto.AddToCartRequest;
import com.ecom.cart.dto.UpdateCartRequest;
import com.ecom.cart.entity.EcomCart;
import com.ecom.cart.exception.ProductNotFoundException;
import com.ecom.cart.repository.EcomCartRepository;
import com.ecom.cart.repository.EcomProductsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EcomCartServiceImpl implements EcomCartService {

    private final EcomCartRepository ecomCartRepository;
    private final EcomProductsRepository ecomProductsRepository;
    private static final String PRODUCT_NOT_FOUND_ERROR = "Cart product with id %d not found";

    @Override
    public EcomCart addToCart(AddToCartRequest addToCartRequest) {
        boolean productExists = ecomProductsRepository.existsById(addToCartRequest.getProductId());
        if (!productExists) {
            throw new ProductNotFoundException(
                    String.format("Product with id %d does not exist", addToCartRequest.getProductId()));
        }
        EcomCart cart = EcomCart.builder()
                .userId(addToCartRequest.getUserId())
                .productId(addToCartRequest.getProductId())
                .quantity(addToCartRequest.getQuantity())
                .build();

        return ecomCartRepository.save(cart);
    }

    @Override
    public EcomCart updateCartProduct(Long productId, UpdateCartRequest updateCartRequest) {
        EcomCart cartProduct = ecomCartRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR, productId)));

        cartProduct.setQuantity(updateCartRequest.getQuantity());
        return ecomCartRepository.save(cartProduct);
    }

    @Override
    @Transactional
    public void deleteCartProduct(Long productId) {

        int deleted = ecomCartRepository.deleteByProductId(productId);
        if (deleted == 0) throw new ProductNotFoundException(String.format(PRODUCT_NOT_FOUND_ERROR, productId));
    }
}
