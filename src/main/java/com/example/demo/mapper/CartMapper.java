package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.item.CartItemDto;
import com.example.demo.dto.item.CartItemResponseDto;
import com.example.demo.dto.item.UpdatedQuantityDto;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface CartMapper {

    UpdatedQuantityDto toUpdatedDto(CartItem cartItem);

    @Mapping(source = "book.id", target = "bookId")
    CartItemResponseDto toCartItemResponseDto(CartItem cartItem);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "cartItems", target = "cartItems")
    CartDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "quantity", target = "quantity")
    CartItemDto toCartItemDto(CartItem cartItem);

    default Set<Long> mapCartItemsToIds(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toSet());
    }

    default Set<CartItemDto> mapCartItemsToDto(Set<CartItem> cartItems) {
        return cartItems.stream()
                .map(this::toCartItemDto)
                .collect(Collectors.toSet());
    }
}
