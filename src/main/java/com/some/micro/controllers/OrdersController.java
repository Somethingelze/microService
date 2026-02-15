package com.some.micro.controllers;

import com.some.micro.model.entities.Order;
import com.some.micro.model.enums.Status;
import com.some.micro.repository.OrdersRepository;
import com.some.micro.repository.UsersRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    OrdersRepository ordersRepository;
    UsersRepository usersRepository;

    public OrdersController(OrdersRepository ordersRepository, UsersRepository usersRepository) {
        this.ordersRepository = ordersRepository;
        this.usersRepository = usersRepository;
    }

    @GetMapping
    public List<Order> getOrders() {
        return ordersRepository.findAll();
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return ordersRepository.findAll();
    }

    @PostMapping
    public Order createOrder(@RequestBody Order order) {
        return ordersRepository.save(order);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id, @RequestParam Status status) {
        Optional<Order> order = ordersRepository.findById(id);
        if (order.isPresent()) {
            order.get().setStatus(status);
            return ordersRepository.save(order.get());
        } else {
            return null;
        }
    }
}
