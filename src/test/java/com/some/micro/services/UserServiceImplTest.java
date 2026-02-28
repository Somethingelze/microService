package com.some.micro.services;

import com.some.micro.mappers.UserMapper;
import com.some.micro.model.dto.UserResponseDto;
import com.some.micro.model.entities.UserEntity;
import com.some.micro.model.enums.Role;
import com.some.micro.repository.UserRepository;
import com.some.micro.services.impl.UserServiceImpl;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    UserMapper userMapper;

    @InjectMocks
    UserServiceImpl userService;

    static final String USERNAME = "test_user";
    static final String RAW_PASSWORD = "password123";
    static final String ENCODED_PASSWORD = "encoded_password123";
    static final UUID USER_ID = UUID.randomUUID();

    @Test
    void getAllUsers_Success() {
        UserEntity entity = new UserEntity();
        UserResponseDto dto = new UserResponseDto(USER_ID, USERNAME, Role.USER);

        when(userRepository.findAll()).thenReturn(List.of(entity));
        when(userMapper.toUserResponseDto(entity)).thenReturn(dto);

        List<UserResponseDto> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(USERNAME, result.getFirst().username());
        verify(userRepository).findAll();
    }

    @Test
    void deleteUserById_Success() {
        userService.deleteUserById(USER_ID);
        verify(userRepository).deleteById(USER_ID);
    }

    @Test
    void registerUser_Success() {
        // Arrange
        when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);

        // Act
        userService.registerUser(USERNAME, RAW_PASSWORD);

        // Assert
        // Используем ArgumentCaptor, чтобы проверить, какой именно объект полетел в save()
        ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(userCaptor.capture());

        UserEntity savedUser = userCaptor.getValue();
        assertEquals(USERNAME, savedUser.getUsername());
        assertEquals(ENCODED_PASSWORD, savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());
    }

    @Test
    void existsByUsername_ShouldReturnTrue_WhenUserExists() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(new UserEntity()));

        boolean exists = userService.existsByUsername(USERNAME);

        assertTrue(exists);
        verify(userRepository).findByUsername(USERNAME);
    }

    @Test
    void existsById_ShouldReturnFalse_WhenUserDoesNotExist() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        boolean exists = userService.existsById(USER_ID);

        assertFalse(exists);
    }

    @Test
    void loadUserByUsername_Success() {
        UserEntity entity = new UserEntity();
        entity.setUsername(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(entity));

        UserDetails result = userService.loadUserByUsername(USERNAME);

        assertNotNull(result);
        assertEquals(USERNAME, result.getUsername());
    }

    @Test
    void loadUserByUsername_NotFound_ThrowsException() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername(USERNAME));
    }
}