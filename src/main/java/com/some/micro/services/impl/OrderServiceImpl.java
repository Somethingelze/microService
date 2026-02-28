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
import com.some.micro.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    UserRepository userRepository;
    Logger log = Logger.getLogger(OrderServiceImpl.class.getName());
    OrderMapper orderMapper;
    SecurityProvider securityProvider;

    @Override
    public List<OrderResponseDto> getAllOrders() {
        log.info("Getting all orders");
        return orderRepository.findAll().stream()
                .map(orderMapper::toOrderResponseDto)
                .toList();
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
        log.info("Deleting order with id: " + id);
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order with id: " + id + " not found");
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrder(UUID id, OrderCreateDto orderCreateDto) {
        log.info("Updating order with id: " + id + " has started");

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