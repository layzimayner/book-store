package com.example.demo.service;

import com.example.demo.dto.item.OrderItemDto;
import com.example.demo.dto.order.ChangeOrderStatusRequestDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.dto.order.PlaceOrderRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderDto saveOrder(PlaceOrderRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .getCartByUserId(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find user's shopping cart"));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new RuntimeException("User with id " + user.getId()
                    + ", has not items in cart");
        }
        return orderMapper.toDto(orderRepository.save(orderMapper.toOrder(shoppingCart,
                requestDto.getShippingAddress(), user)));
    }

    @Override
    public Page<OrderDto> findOrders(Pageable pageable, Long userId) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public OrderDto changeStatus(Long orderId, ChangeOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Can't update order with id "
                        + orderId + " because it does not exist")
        );
        order.setStatus(requestDto.status());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderItemDto> findItemsByOrderId(Long orderId, Pageable pageable, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order not found or does not belong to the user"));
        List<OrderItemDto> orderItemDto = order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
        return new PageImpl<>(orderItemDto, pageable, orderItemDto.size());
    }

    @Override
    public OrderItemDto findItemFormOrder(Long orderId, Long itemId, Long userId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderIdAndUserId(itemId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Order item not found or does not belong to the user"));
        return orderItemMapper.toDto(orderItem);
    }
}
