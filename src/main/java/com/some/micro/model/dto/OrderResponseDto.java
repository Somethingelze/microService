package com.some.micro.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrderResponseDto(
        @NotBlank(message = "Описание не может быть пустым")
        @Size(max = 255, message = "Описание слишком длинное")
        String description
) {}