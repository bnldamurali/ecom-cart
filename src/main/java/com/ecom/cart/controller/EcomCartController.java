package com.ecom.cart.controller;

import com.ecom.cart.dto.AddToCartRequest;
import com.ecom.cart.dto.UpdateCartRequest;
import com.ecom.cart.entity.EcomCart;
import com.ecom.cart.service.EcomCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class EcomCartController {

    @Autowired
    private EcomCartService ecomCartService;

    // Add product to cart
    @PostMapping("/products")
    public ResponseEntity<EcomCart> addToCart(@RequestBody AddToCartRequest addToCartRequest) {
        return ResponseEntity.ok(ecomCartService.addToCart(addToCartRequest));
    }

    // Update Product
    @PutMapping("/products/{productId}")
    public ResponseEntity<EcomCart> updateCartItem(
            @PathVariable Long productId,
            @RequestBody UpdateCartRequest updateCartRequest) {
        return ResponseEntity.ok(ecomCartService.updateCartProduct(productId, updateCartRequest));
    }

    // Delete Product
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long productId) {
        ecomCartService.deleteCartProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
