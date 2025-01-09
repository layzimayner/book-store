package com.example.demo.repository;

import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    Page<Order> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT i FROM OrderItem i WHERE i.order.id IN :orderIds")
    List<OrderItem> findItemsByOrderIds(@Param("orderIds") List<Long> orderIds);

    Optional<Order> findByIdAndUserId(Long orderId, Long userId);
}
