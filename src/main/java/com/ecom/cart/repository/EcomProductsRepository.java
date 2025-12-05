package com.ecom.cart.repository;

import com.ecom.cart.entity.Products;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EcomProductsRepository extends JpaRepository<Products, Long> {
}
