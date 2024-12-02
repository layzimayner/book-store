package com.example.demo.controller;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.item.CreateItemRequestDro;
import com.example.demo.dto.item.UpdateItemRequestDto;
import com.example.demo.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "cart management", description = "Endpoints for management cart")
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
@Validated
public class CartController {
    private final CartService cartService;

    @GetMapping
    public CartDto getCart(Authentication authentication) {
        return cartService.getCart(authentication);
    }

    @PostMapping
    public CartDto addItemToCart(@RequestBody @Valid CreateItemRequestDro requestDro,
                                 Authentication authentication) {
        return cartService.addItemToCart(requestDro, authentication);
    }

    @PutMapping("/{cartItemId}")
    public CartDto updateItemQuantity(@PathVariable Long id,
                                      @RequestBody @Valid UpdateItemRequestDto requestDro,
                                      Authentication authentication) {
        return cartService.updateItemQuantity(id, requestDro, authentication);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{cartItemId}")
    public void deleteItemFromCart(@PathVariable Long id,
                                   Authentication authentication) {
        cartService.deleteItemFromCart(id, authentication);
    }
}
