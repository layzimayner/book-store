package com.example.demo.service;

import com.example.demo.dto.item.OrderItemDto;
import com.example.demo.dto.order.ChangeOrderStatusRequestDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.dto.order.PlaceOrderRequestDto;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    void saveOrder(PlaceOrderRequestDto requestDto, User user);

    Page<OrderDto> findOrders(Pageable pageable, Long userId);

    void changeStatus(Long orderId, ChangeOrderStatusRequestDto requestDto);

    Page<OrderItemDto> findItemsByOrderId(Long orderId, Pageable pageable, Long userId);

    OrderItemDto findItemFormOrder(Long orderId, Long itemId, Long userId);
}
