package com.ecom.cart.service;

import com.ecom.cart.dto.AddToCartRequest;
import com.ecom.cart.dto.UpdateCartRequest;
import com.ecom.cart.entity.EcomCart;
import com.ecom.cart.exception.ProductNotFoundException;
import com.ecom.cart.repository.EcomCartRepository;
import com.ecom.cart.repository.EcomProductsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EcomCartServiceTest {

    @Mock
    private EcomCartRepository ecomCartRepository;

    @Mock
    private EcomProductsRepository ecomProductsRepository;

    @InjectMocks
    private EcomCartServiceImpl ecomCartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddToCart_Success() {
        AddToCartRequest request = new AddToCartRequest(1L, 10L, 2);
        EcomCart savedCart = new EcomCart();
        savedCart.setId(1L);
        savedCart.setUserId(1L);
        savedCart.setProductId(10L);
        savedCart.setQuantity(2);

        when(ecomCartRepository.save(any(EcomCart.class))).thenReturn(savedCart);
        when(ecomProductsRepository.existsById(anyLong())).thenReturn(true);

        EcomCart result = ecomCartService.addToCart(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getUserId());
        assertEquals(10L, result.getProductId());
        assertEquals(2, result.getQuantity());

        verify(ecomCartRepository, times(1)).save(any(EcomCart.class));
    }

    @Test
    void testUpdateCartProduct_Success() {
        UpdateCartRequest request = UpdateCartRequest.builder().quantity(5).build();

        EcomCart existingCart = new EcomCart();
        existingCart.setId(1L);
        existingCart.setProductId(10L);
        existingCart.setUserId(1L);
        existingCart.setQuantity(2);

        when(ecomCartRepository.findByProductId(10L)).thenReturn(Optional.of(existingCart));
        when(ecomCartRepository.save(existingCart)).thenReturn(existingCart);

        EcomCart result = ecomCartService.updateCartProduct(10L, request);

        assertNotNull(result);
        assertEquals(5, result.getQuantity());
        verify(ecomCartRepository, times(1)).findByProductId(10L);
        verify(ecomCartRepository, times(1)).save(existingCart);
    }

    @Test
    void testUpdateCartProduct_ProductNotFound() {
        UpdateCartRequest request = UpdateCartRequest.builder().quantity(5).build();

        when(ecomCartRepository.findByProductId(10L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> ecomCartService.updateCartProduct(10L, request));

        assertEquals("Cart product with id 10 not found", exception.getMessage());
        verify(ecomCartRepository, times(1)).findByProductId(10L);
        verify(ecomCartRepository, never()).save(any());
    }

    @Test
    void testDeleteCartProduct_Success() {
        when(ecomCartRepository.deleteByProductId(10L)).thenReturn(1);

        assertDoesNotThrow(() -> ecomCartService.deleteCartProduct(10L));

        verify(ecomCartRepository, times(1)).deleteByProductId(10L);
    }

    @Test
    void testDeleteCartProduct_NotFound() {
        when(ecomCartRepository.deleteByProductId(10L)).thenReturn(0);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> ecomCartService.deleteCartProduct(10L));

        assertEquals("Cart product with id 10 not found", exception.getMessage());
        verify(ecomCartRepository, times(1)).deleteByProductId(10L);
    }
}
