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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private SecurityProvider securityProvider;

    @InjectMocks
    private OrderServiceImpl orderService;

    static final UUID ID = UUID.randomUUID();
    static final String USERNAME = "test_user";

    @Test
    void getAllOrders_ShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 5);

        OrderEntity entity = new OrderEntity();
        OrderResponseDto dto = new OrderResponseDto("Test Description");

        Page<OrderEntity> entityPage = new PageImpl<>(List.of(entity));

        Mockito.when(orderRepository.findAll(Mockito.any(Pageable.class))).thenReturn(entityPage);
        Mockito.when(orderMapper.toOrderResponseDto(entity)).thenReturn(dto);

        Page<OrderResponseDto> result = orderService.getAllOrders(pageable);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals("Test Description", result.getContent().getFirst().description());

        Mockito.verify(orderRepository).findAll(pageable);
    }

    @Test
    void getOrderById_WhenNotFound_ThrowsException() {
        Mockito.when(orderRepository.findById(ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(ID));
        Mockito.verifyNoInteractions(orderMapper);
    }


    @Test
    void deleteOrder_Success() {
        Mockito.when(orderRepository.existsById(ID)).thenReturn(true);

        orderService.deleteOrder(ID);

        Mockito.verify(orderRepository).deleteById(ID);
    }

    @Test
    void deleteOrder_NotFound_ThrowsException() {
        Mockito.when(orderRepository.existsById(ID)).thenReturn(false);

        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(ID));
        Mockito.verify(orderRepository, Mockito.never()).deleteById( ArgumentMatchers.any(UUID.class));
    }


    @Test
    void createOrder_Success() {
        OrderCreateDto createDto = new OrderCreateDto("New Order");
        UserEntity user = new UserEntity();
        OrderEntity order = new OrderEntity();
        OrderResponseDto expected = new OrderResponseDto("New Order");

        Mockito.when(securityProvider.getUsername()).thenReturn(USERNAME);
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        Mockito.when(orderMapper.toOrderEntity(createDto)).thenReturn(order);
        Mockito.when(orderRepository.save(ArgumentMatchers.any())).thenReturn(order);
        Mockito.when(orderMapper.toOrderResponseDto(ArgumentMatchers.any())).thenReturn(expected);

        OrderResponseDto actual = orderService.createOrder(createDto);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.description(), actual.description());
        Mockito.verify(orderRepository).save(ArgumentMatchers.any());
    }

    @Test
    void createOrder_UserNotFound_ThrowsException() {
        Mockito.when(securityProvider.getUsername()).thenReturn(USERNAME);
        Mockito.when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> orderService.createOrder(new OrderCreateDto("Desc")));
    }
}