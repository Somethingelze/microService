package com.some.micro.model.dto;

import java.time.LocalDateTime;

public record ErrorResponseDto(
        String message,
        String errorCode,
        int status,
        String path,
        String traceId,
        LocalDateTime timestamp
) {
}