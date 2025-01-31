package com.example.demo.controller;

import com.example.demo.dto.item.OrderItemDto;
import com.example.demo.dto.order.ChangeOrderStatusRequestDto;
import com.example.demo.dto.order.OrderDto;
import com.example.demo.dto.order.PlaceOrderRequestDto;
import com.example.demo.model.User;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "orders management", description = "Endpoints for management orders")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Place new order", description = "Add new order to DB")
    public OrderDto placeAnOrder(@RequestBody @Valid PlaceOrderRequestDto requestDto,
                             Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.saveOrder(requestDto, user);
    }

    @GetMapping
    @Operation(summary = "Get all user's orders", description = "Get page of all user's orders")
    public Page<OrderDto> getOrders(Pageable pageable, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findOrders(pageable, user.getId());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{orderId}")
    @Operation(summary = "Update order status",
            description = "Set status executed for order, by id")
    public OrderDto updateOrderStatus(@PathVariable @Positive Long orderId,
                                  @RequestBody @Valid ChangeOrderStatusRequestDto requestDto) {
        return orderService.changeStatus(orderId, requestDto);
    }

    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all items from order",
            description = "Get all items from order, by id")
    public Page<OrderItemDto> getItemsByOrderId(@PathVariable @Positive Long orderId,
                                                Pageable pageable,
                                                Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findItemsByOrderId(orderId, pageable, user.getId());
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get item from order", description = "Get item from order, by id")
    public OrderItemDto getItemFormOrder(@PathVariable @Positive Long orderId,
                                                  @PathVariable @Positive Long itemId,
                                                  Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.findItemFormOrder(orderId,itemId,user.getId());
    }
}
