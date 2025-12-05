package com.ecom.cart.repository;

import com.ecom.cart.entity.EcomCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcomCartRepository extends JpaRepository<EcomCart, Long> {
}
