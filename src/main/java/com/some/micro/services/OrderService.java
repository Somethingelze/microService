package com.some.micro.services;

import com.some.micro.model.dto.OrderCreateDto;
import com.some.micro.model.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface OrderService {

    Page<OrderResponseDto> getAllOrders(Pageable pageable);
    OrderResponseDto getOrderById(UUID id);
    OrderResponseDto createOrder(OrderCreateDto orderCreateDto);
    void deleteOrder(UUID id);
    OrderResponseDto updateOrder(UUID id, OrderCreateDto orderCreateDto);

    }
