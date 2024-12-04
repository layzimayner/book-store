package com.example.demo.repository;

import com.example.demo.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @Query("SELECT s FROM ShoppingCart s LEFT JOIN FETCH s.cartItems c WHERE s.id = :userId")
    Optional<ShoppingCart> getCartByUserId(@Param("userId") Long userId);
}
