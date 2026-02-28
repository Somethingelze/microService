package com.some.micro.services;

import com.some.micro.model.dto.OrderCreateDto;
import com.some.micro.model.dto.OrderResponseDto;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    List<OrderResponseDto> getAllOrders();
    OrderResponseDto getOrderById(UUID id);
    OrderResponseDto createOrder(OrderCreateDto orderCreateDto);
    void deleteOrder(UUID id);
    OrderResponseDto updateOrder(UUID id, OrderCreateDto orderCreateDto);

    }
