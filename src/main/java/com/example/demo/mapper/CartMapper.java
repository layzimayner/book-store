package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.cart.CartDto;
import com.example.demo.model.ShoppingCart;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CartMapper {
    CartDto toDto(ShoppingCart soppingCart);
}
