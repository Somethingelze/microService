package com.some.micro.services;

import com.some.micro.exceptions.OrderNotFoundException;
import com.some.micro.model.entities.Order;
import com.some.micro.repository.OrdersRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

@Service
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final Logger log = Logger.getLogger(OrderService.class.getName());

    public OrderService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public List<Order> getAllOrders() {
        log.info("Getting all orders");
        return ordersRepository.findAll();
    }

    public Order getOrderById(Long id) {
        log.info("Getting order by id: " + id);
        return ordersRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + "doesn't exist"));
    }

    public Order createOrder(Order order) {
        log.info("Creating order: " + order);
        return ordersRepository.save(order);
    }

    public Order updateOrder(Long id, Order order) {
        Order existingOrder = ordersRepository.findById(id).orElseThrow(() -> new OrderNotFoundException("Order with id: " + id + "doesn't exist"));
        existingOrder.setStatus(order.getStatus());
        existingOrder.setDescription(order.getDescription());
        existingOrder.setUser(order.getUser());
        existingOrder.setUpdatedAt(LocalDateTime.now());

        log.info("Updating order: " + order);
        return ordersRepository.save(existingOrder);
    }

    @Transactional
    public ResponseEntity<String> deleteOrder(Long id) {
        log.info("Deleting order with id: " + id);
        ordersRepository.deleteById(id);
        return ResponseEntity.ok().body("Order with id: " + id + " has been deleted");
    }
}
