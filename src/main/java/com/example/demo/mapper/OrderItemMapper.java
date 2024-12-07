package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.item.OrderItemDto;
import com.example.demo.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);
}
