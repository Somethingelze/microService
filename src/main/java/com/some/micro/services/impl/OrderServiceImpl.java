package com.some.micro.services.impl;

import com.some.micro.exceptions.OrderNotFoundException;
import com.some.micro.mappers.OrderMapper;
import com.some.micro.model.dto.OrderCreateDto;
import com.some.micro.model.dto.OrderResponseDto;
import com.some.micro.model.entities.OrderEntity;
import com.some.micro.model.enums.Status;
import com.some.micro.repository.OrderRepository;
import com.some.micro.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    Logger log = Logger.getLogger(OrderServiceImpl.class.getName());
    OrderMapper orderMapper;

    @Override
    public List<OrderResponseDto> getAllOrders() {
        log.info("Getting all orders");
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponseDto)
                .toList();
    }

    @Override
    public OrderResponseDto getOrderById(UUID id) {
        log.info("Getting order by id: " + id);
        return orderMapper.toOrderResponseDto(orderRepository.findById(id));
    }

    @Override
    @Transactional
    public OrderEntity createOrder(OrderCreateDto orderCreateDto) {
        log.info("Creating order: " + orderCreateDto.description());
        OrderEntity order = orderMapper.toOrderEntity((orderCreateDto));
        order.setStatus(Status.CREATED);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(UUID id) {
        log.info("Deleting order with id: " + id);
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found");
        }
        orderRepository.deleteById(id);
    }

    @Override
    public OrderResponseDto updateOrder(UUID id, OrderCreateDto orderCreateDto) {
        log.info("Updating order with id: " + id);
        OrderEntity order = orderRepository.findById(id);
        if (order == null) {
            throw new OrderNotFoundException("Order with id: " + id + " not found");
        } else {
            return orderMapper.toOrderResponseDto(orderRepository.save(order));
        }
    }
}