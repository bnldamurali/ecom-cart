package com.ecom.cart.controller;

import com.ecom.cart.dto.AddToCartRequest;
import com.ecom.cart.dto.UpdateCartRequest;
import com.ecom.cart.entity.EcomCart;
import com.ecom.cart.service.EcomCartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EcomCartController.class)
class EcomCartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EcomCartService ecomCartService;

    @Test
    void test_AddToCart() throws Exception {

        AddToCartRequest request = new AddToCartRequest(1L, 10L, 2);
        EcomCart cart = new EcomCart(1L, 1L, 10L, 2);

        when(ecomCartService.addToCart(any())).thenReturn(cart);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.productId").value(10))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void test_UpdateCartProduct() throws Exception {

        UpdateCartRequest request = UpdateCartRequest
                .builder()
                .quantity(5)
                .build();
        EcomCart updatedCart = new EcomCart(1L, 1L, 10L, 5);

        when(ecomCartService.updateCartProduct(any(), any()))
                .thenReturn(updatedCart);

        mockMvc.perform(put("/api/v1/products/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void test_DeleteCartProduct() throws Exception {

        doNothing().when(ecomCartService).deleteCartProduct(10L);

        mockMvc.perform(delete("/api/v1/products/10"))
                .andExpect(status().isNoContent());
    }

    @Test
    void test_ServiceException() throws Exception {

        when(ecomCartService.addToCart(any()))
                .thenThrow(new RuntimeException("Unexpected error"));

        AddToCartRequest request = new AddToCartRequest(1L, 10L, 2);

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }
}
