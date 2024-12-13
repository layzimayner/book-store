package com.example.demo.repository;

import com.example.demo.model.OrderItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrderIdAndOrder_User_Id(Long orderId, Long itemId, Long userId);
}
