package com.some.micro.controllers;

import com.some.micro.exceptions.OrderNotFoundException;
import com.some.micro.exceptions.UserNotFoundException;
import com.some.micro.model.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleOrderNotFound(OrderNotFoundException ex, HttpServletRequest request) {
        log.error("Order not found exception: {}", ex.getMessage());
        return createErrorResponse(ex.getMessage(),
                "ORDER_NOT_FOUND",
                404,
                request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponseDto handleForbidden(AccessDeniedException ex, HttpServletRequest request) {
        return createErrorResponse(ex.getMessage(),
                "ACCESS_DENIED",
                403,
                request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleUserNameNotFound(UsernameNotFoundException ex, HttpServletRequest request) {
        return createErrorResponse(ex.getMessage(),
                "USERNAME_NOT_FOUND",
                404,
                request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleUserNotFound(UserNotFoundException ex, HttpServletRequest request) {
        return createErrorResponse(ex.getMessage(),
                "USER_NOT_FOUND",
                404,
                request);
    }

    private ErrorResponseDto createErrorResponse(String message, String errorCode, int status, HttpServletRequest request) {
        return new ErrorResponseDto(
                message,
                errorCode,
                status,
                request.getRequestURI(),
                MDC.get("traceId"),
                LocalDateTime.now()
        );
    }
}
