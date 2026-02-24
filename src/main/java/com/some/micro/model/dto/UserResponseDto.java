package com.some.micro.model.dto;

import com.some.micro.model.enums.Role;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        Role role
) {}