package com.some.micro.services;

import com.some.micro.model.dto.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.UUID;

public interface UserService extends UserDetailsService {

    List<UserResponseDto> getAllUsers();
    boolean existsByUsername(String username);
    boolean existsById(UUID id);
    void deleteUserById(UUID id);

    void registerUser(String username, String rawPassword);
    UserDetails loadUserByUsername(String username);
    }