package com.some.micro.services;

import com.some.micro.model.dto.OrderCreateDto;
import com.some.micro.model.dto.OrderResponseDto;
import com.some.micro.model.entities.OrderEntity;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderResponseDto> getAllOrders();
    OrderResponseDto getOrderById(UUID id);
    OrderEntity createOrder(OrderCreateDto orderCreateDto);
    void deleteOrder(UUID id);
    OrderResponseDto updateOrder(UUID id, OrderCreateDto orderCreateDto);

    }
