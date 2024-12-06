package com.example.demo.repository;

import com.example.demo.model.OrderItem;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT i FROM OrderItem i JOIN i.orderId o WHERE i.orderId = :orderId")
    Page<OrderItem> findByOrderId(@Param("orderId") Long orderId, Pageable pageable);

    Optional<OrderItem> findByItemId(Long itemId);
}
