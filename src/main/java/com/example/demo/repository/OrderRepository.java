package com.example.demo.repository;

import com.example.demo.model.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems i WHERE o.id = :orderId")
    Page<Order> findAllByUserId(@Param("orderId") Long id, Pageable pageable);

    @EntityGraph(value = "Order.withOrderItems", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId")
    Optional<Order> findByIdIncludeUserId(@Param("orderId") Long orderId);
}
