package com.some.micro.services;

import com.some.micro.model.dto.UserResponseDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<UserResponseDto> getAllUsers();
    void deleteUserById(Long id);
    boolean existsByUsername(String username);
    boolean existsById(Long id);
    void registerUser(String username, String rawPassword);
    UserDetails loadUserByUsername(String username);
    }