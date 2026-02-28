package com.some.micro.services;

import com.some.micro.configuration.SecurityProvider;
import com.some.micro.exceptions.OrderNotFoundException;
import com.some.micro.mappers.OrderMapper;
import com.some.micro.model.dto.OrderCreateDto;
import com.some.micro.model.dto.OrderResponseDto;
import com.some.micro.model.entities.OrderEntity;
import com.some.micro.model.entities.UserEntity;
import com.some.micro.repository.OrderRepository;
import com.some.micro.repository.UserRepository;
import com.some.micro.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    OrderMapper orderMapper;
    @Mock
    SecurityProvider securityProvider;

    @InjectMocks
    OrderServiceImpl orderService;

    static final UUID ID = UUID.randomUUID();
    static final String USERNAME = "test_user";

    @Test
    void getAllOrders_Success() {
        OrderEntity entity = new OrderEntity();
        OrderResponseDto dto = new OrderResponseDto("Test");

        when(orderRepository.findAll()).thenReturn(List.of(entity));
        when(orderMapper.toOrderResponseDto(entity)).thenReturn(dto);

        List<OrderResponseDto> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    void getOrderById_WhenNotFound_ThrowsException() {
        when(orderRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(ID));
        verifyNoInteractions(orderMapper);
    }


    @Test
    void deleteOrder_Success() {
        when(orderRepository.existsById(ID)).thenReturn(true);

        orderService.deleteOrder(ID);

        verify(orderRepository).deleteById(ID);
    }

    @Test
    void deleteOrder_NotFound_ThrowsException() {
        when(orderRepository.existsById(ID)).thenReturn(false);

        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(ID));
        verify(orderRepository, never()).deleteById((UUID) any());
    }


    @Test
    void createOrder_Success() {
        OrderCreateDto createDto = new OrderCreateDto("New Order");
        UserEntity user = new UserEntity();
        OrderEntity order = new OrderEntity();
        OrderResponseDto expected = new OrderResponseDto("New Order");

        when(securityProvider.getUsername()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(orderMapper.toOrderEntity(createDto)).thenReturn(order);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderMapper.toOrderResponseDto(any())).thenReturn(expected);

        OrderResponseDto actual = orderService.createOrder(createDto);

        assertNotNull(actual);
        assertEquals(expected.description(), actual.description());
        verify(orderRepository).save(any());
    }

    @Test
    void createOrder_UserNotFound_ThrowsException() {
        when(securityProvider.getUsername()).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> orderService.createOrder(new OrderCreateDto("Desc")));
    }
}