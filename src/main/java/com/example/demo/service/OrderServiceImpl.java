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
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;

    @Override
    public void saveOrder(PlaceOrderRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository
                .getCartByUserId(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("Can't find user's shopping cart"));
        orderRepository.save(orderMapper.toOrder(shoppingCart,
                requestDto.getShippingAddress(), user));
    }

    @Override
    public Page<OrderDto> findOrders(Pageable pageable, Long userId) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toDto);
    }

    @Override
    public void changeStatus(Long orderId, ChangeOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException("Can't update order with id "
                        + orderId + " because it does not exist")
        );
        order.setStatus(requestDto.status());
        orderRepository.save(order);
    }

    @Override
    public Page<OrderItemDto> findItemsByOrderId(Long orderId, Pageable pageable, Long userId) {
        Order order = orderRepository.findByIdIncludeUserId(orderId).orElseThrow(() ->
                new EntityNotFoundException("Can't find order with id " + orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Order don't belongs to user");
        }
        return orderItemRepository.findByOrderId(orderId, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemDto findItemFormOrder(Long orderId, Long itemId, Long userId) {
        Order order = orderRepository.findByIdIncludeUserId(orderId).orElseThrow(() ->
                new EntityNotFoundException("Can't find order with id " + orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("Order don't belongs to user");
        }
        OrderItem orderItem = orderItemRepository.findByItemId(itemId).orElseThrow(() ->
                new EntityNotFoundException("Can't find item with id " + itemId));
        return orderItemMapper.toDto(orderItem);
    }
}
