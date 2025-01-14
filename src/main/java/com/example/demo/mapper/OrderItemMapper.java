package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.item.OrderItemDto;
import com.example.demo.model.OrderItem;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(target = "orderItemId", source = "id")
    @Mapping(target = "bookId", source = "book.id")
    OrderItemDto toDto(OrderItem orderItem);

    List<OrderItemDto> toDtos(List<OrderItem> orderItems);
}
