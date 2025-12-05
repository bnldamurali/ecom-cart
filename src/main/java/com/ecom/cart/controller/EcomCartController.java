package com.ecom.cart.controller;

import com.ecom.cart.dto.AddToCartRequest;
import com.ecom.cart.dto.EcomCartResponse;
import com.ecom.cart.dto.UpdateCartRequest;
import com.ecom.cart.entity.EcomCart;
import com.ecom.cart.service.EcomCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "EComCart", description = "ECom Cart API")
@Validated
@RestController
@RequestMapping("/api/v1")
public class EcomCartController {

    private final EcomCartService ecomCartService;

    public EcomCartController(EcomCartService ecomCartService) {
        this.ecomCartService = ecomCartService;
    }

    @Operation(
            summary = "Add Product to Cart",
            description = "Add a new product to the shopping cart",
            method = "POST",
            operationId = "addToCart",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AddToCartRequest.class),
                            examples = @ExampleObject(value = "{ \"userId\": 1, \"productId\": 1001, \"quantity\": 2 }")
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product added to cart",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EcomCartResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content)
    })
    @PostMapping("/products")
    public ResponseEntity<EcomCartResponse> addToCart(@Valid @RequestBody AddToCartRequest request) {
        EcomCart ecomCart = ecomCartService.addToCart(request);

        EcomCartResponse response = mapToResponse(ecomCart);

        return ResponseEntity
                .created(URI.create("/api/v1/products/" + response.getProductId()))
                .body(response);
    }

    @Operation(
            summary = "Update Product Quantity",
            description = "Update the quantity of a product in the cart",
            method = "PUT",
            operationId = "updateCartProduct",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UpdateCartRequest.class),
                            examples = @ExampleObject(value = "{ \"quantity\": 5 }")
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product quantity updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EcomCartResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content)
    })
    @PutMapping("/products/{productId}")
    public ResponseEntity<EcomCartResponse> updateCartProduct(
            @PathVariable @NotNull @Positive Long productId,
            @Valid @RequestBody UpdateCartRequest request) {

        EcomCart ecomCart = ecomCartService.updateCartProduct(productId, request);

        return ResponseEntity.ok(mapToResponse(ecomCart));
    }

    @Operation(
            summary = "Remove Product from Cart",
            description = "Remove a product from the shopping cart by productId",
            method = "DELETE",
            operationId = "deleteCartProduct"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product removed from cart"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content)
    })
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteCartProduct(@PathVariable Long productId) {
        ecomCartService.deleteCartProduct(productId);
        return ResponseEntity.noContent().build();
    }

    private EcomCartResponse mapToResponse(EcomCart cart) {
        return EcomCartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .productId(cart.getProductId())
                .quantity(cart.getQuantity())
                .build();
    }
}
