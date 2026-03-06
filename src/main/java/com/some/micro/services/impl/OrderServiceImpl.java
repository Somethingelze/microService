package com.some.micro.services.impl;

import com.some.micro.configuration.SecurityProvider;
import com.some.micro.exceptions.OrderNotFoundException;
import com.some.micro.mappers.OrderMapper;
import com.some.micro.model.dto.OrderCreateDto;
import com.some.micro.model.dto.OrderResponseDto;
import com.some.micro.model.entities.OrderEntity;
import com.some.micro.model.enums.Status;
import com.some.micro.repository.OrderRepository;
import com.some.micro.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements com.some.micro.services.OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    private final SecurityProvider securityProvider;

    @Override
    public Page<OrderResponseDto> getAllOrders(Pageable pageable) {
        log.info("Getting all orders");

        return orderRepository.findAll(pageable)
                .map(orderMapper::toOrderResponseDto);
    }

    @Override
    public OrderResponseDto getOrderById(UUID id) {
        return orderRepository.findById(id)
                .map(orderMapper::toOrderResponseDto)
                .orElseThrow(() -> new OrderNotFoundException("Order with id:  " + id + " not found"));
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(OrderCreateDto orderCreateDto) {
        log.info("Creating order: " + orderCreateDto.description());

        String username = securityProvider.getUsername();
        OrderEntity order = orderMapper.toOrderEntity(orderCreateDto);

        return userRepository.findByUsername(username)
                .map(user -> {
                    order.setDescription(orderCreateDto.description());
                    order.setStatus(Status.CREATED);
                    order.setUser(user);
                    log.info("Order " + order.getDescription() + " was created");
                    return orderMapper.toOrderResponseDto(orderRepository.save(order));
                })
                .orElseThrow(() -> new UsernameNotFoundException("User with name" + username + " not found"));
    }

    @Override
    @Transactional
    public void deleteOrder(UUID id) {
        log.info("Starting deletion process for order: {}", id); // Здесь traceId подставится сам

        if (!orderRepository.existsById(id)) {
            log.error("Deletion failed: order {} not found", id);
            throw new OrderNotFoundException("Order with id: " + id + " not found");
        }

        orderRepository.deleteById(id);
        log.info("Order {} successfully deleted", id);
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrder(UUID id, OrderCreateDto orderCreateDto) {
        log.info("Starting updating order with id: " + id + " has started");

        return orderRepository.findById(id)
                .map(
                        order -> {
                            order.setDescription(orderCreateDto.description());
                            log.info("Updating order with id: " + id + " has finished");
                            return orderMapper.toOrderResponseDto(order);
                        })
                .orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + " not found"));

    }
}