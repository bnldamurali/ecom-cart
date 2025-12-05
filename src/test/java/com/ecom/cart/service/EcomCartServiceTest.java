package com.ecom.cart.service;

import com.ecom.cart.dto.AddToCartRequest;
import com.ecom.cart.dto.UpdateCartRequest;
import com.ecom.cart.entity.EcomCart;
import com.ecom.cart.repository.EcomCartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EcomCartServiceTest {

    @Mock
    private EcomCartRepository ecomCartRepository;

    @InjectMocks
    private EcomCartService ecomCartService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddProductToCart_Success() {
        AddToCartRequest addToCartRequest = AddToCartRequest.builder()
                .productId(1L)
                .quantity(3)
                .userId(10L)
                .build();

        EcomCart savedCart = EcomCart.builder()
                .id(100L)
                .userId(1L)
                .productId(10L)
                .quantity(3)
                .build();

        when(ecomCartRepository.save(any(EcomCart.class))).thenReturn(savedCart);

        EcomCart result = ecomCartService.addToCart(addToCartRequest);

        assertEquals(100L, result.getId());
        verify(ecomCartRepository, times(1)).save(any(EcomCart.class));
    }

    @Test
    public void testUpdateProductQuantity_Success() {
        Long cartId = 1L;

        EcomCart existingCart = EcomCart.builder()
                .id(cartId)
                .userId(1L)
                .productId(10L)
                .quantity(2)
                .build();
        UpdateCartRequest updateCartRequest = UpdateCartRequest.builder()
                .quantity(5)
                .build();

        when(ecomCartRepository.findById(cartId)).thenReturn(Optional.of(existingCart));

        EcomCart updatedResult = ecomCartService.updateCartProduct(cartId, updateCartRequest);

        assertEquals(5, updatedResult.getQuantity());
        verify(ecomCartRepository, times(1)).save(existingCart);
    }

    @Test
    public void testDeleteProductFromCart_Success() {
        EcomCart existing = EcomCart.builder()
                .id(1L)
                .userId(1L)
                .productId(2L)
                .quantity(3)
                .build();

        when(ecomCartRepository.findById(1L)).thenReturn(Optional.of(existing));

        ecomCartService.deleteCartProduct(1L);

        verify(ecomCartRepository, times(1)).delete(existing);
    }
}
