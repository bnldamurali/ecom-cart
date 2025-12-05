package com.ecom.cart.controller;

import com.ecom.cart.dto.AddToCartRequest;
import com.ecom.cart.dto.UpdateCartRequest;
import com.ecom.cart.entity.EcomCart;
import com.ecom.cart.service.EcomCartService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EcomCartController.class)
public class EcomCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EcomCartService ecomCartService;

    @Test
    public void testAddToCart() throws Exception {

        EcomCart saved = EcomCart.builder()
                .id(1L)
                .userId(1L)
                .productId(10L)
                .quantity(3)
                .build();

        when(ecomCartService.addToCart(any(AddToCartRequest.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "userId": 1,
                          "productId": 10,
                          "quantity": 3
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateQuantity() throws Exception {
        UpdateCartRequest updateCartRequest = UpdateCartRequest.builder()
                .quantity(5)
                .build();

        EcomCart updated = EcomCart.builder()
                .id(1L)
                .quantity(5)
                .build();

        when(ecomCartService.updateCartProduct(1L, updateCartRequest)).thenReturn(updated);

        mockMvc.perform(put("/api/v1/products/1?quantity=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5));
    }
}
