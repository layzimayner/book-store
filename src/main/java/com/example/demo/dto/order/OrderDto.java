package com.example.demo.dto.order;

import com.example.demo.dto.item.OrderItemDto;
import com.example.demo.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {
    private Long orderId;
    private Long userId;
    private List<OrderItemDto> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Order.Status status;
}
