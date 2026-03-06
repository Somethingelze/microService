package com.some.micro.controllers;

import com.some.micro.model.dto.OrderCreateDto;
import com.some.micro.model.dto.OrderResponseDto;
import com.some.micro.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;


import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrdersController {

    private final OrderService orderService;

    @GetMapping()
    public Page<OrderResponseDto> getAllOrders(@ParameterObject Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrderById(@PathVariable UUID id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public OrderResponseDto createOrder(@Valid @RequestBody OrderCreateDto orderCreateDto) {
        return orderService.createOrder(orderCreateDto);
    }

    @PutMapping("/{id}")
    public OrderResponseDto updateOrder(@PathVariable UUID id, @Valid @RequestBody OrderCreateDto orderCreateDto) {
        return orderService.updateOrder(id, orderCreateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
    }
}
