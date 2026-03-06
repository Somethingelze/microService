package com.some.micro.mappers;

import com.some.micro.model.dto.OrderCreateDto;
import com.some.micro.model.dto.OrderResponseDto;
import com.some.micro.model.entities.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponseDto toOrderResponseDto(OrderEntity orderEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrderEntity toOrderEntity(OrderCreateDto orderCreateDto);
}
