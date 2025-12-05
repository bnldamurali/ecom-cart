package com.ecom.cart.repository;

import com.ecom.cart.entity.EcomCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EcomCartRepository extends JpaRepository<EcomCart, Long> {

    Optional<EcomCart> findByProductId(Long productId);

    boolean existsByProductId(Long productId);

    int deleteByProductId(Long productId);
}
