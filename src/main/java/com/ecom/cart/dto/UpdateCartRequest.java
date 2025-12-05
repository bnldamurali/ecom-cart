package com.ecom.cart.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCartRequest {
    private Integer quantity;
}