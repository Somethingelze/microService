package com.some.micro.controllers;

import com.some.micro.model.dto.OrderCreateDto;
import com.some.micro.model.dto.OrderResponseDto;
import com.some.micro.model.entities.OrderEntity;
import com.some.micro.services.impl.OrderServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    OrderServiceImpl orderServiceImpl;

    public OrdersController(OrderServiceImpl orderServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
    }

    @GetMapping()
    public List<OrderResponseDto> getAllOrders() {
        return orderServiceImpl.getAllOrders();
    }

    @GetMapping("/{id}")
    public OrderResponseDto getAllOrders(@PathVariable UUID id) {
        return orderServiceImpl.getOrderById(id);
    }

    @PostMapping
    public OrderEntity createOrder(@RequestBody OrderCreateDto orderCreateDto) {
        return orderServiceImpl.createOrder(orderCreateDto);
    }

    @PutMapping("/{id}")
    public OrderResponseDto updateOrder(@PathVariable UUID id, @RequestBody OrderCreateDto orderCreateDto) {
        return orderServiceImpl.updateOrder(id, orderCreateDto);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable UUID id) {
        orderServiceImpl.deleteOrder(id);
    }
}
