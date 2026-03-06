package com.some.micro.handlers;

import com.some.micro.model.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                "Full authentication is required to access this resource",
                "UNAUTHORIZED",
                HttpServletResponse.SC_UNAUTHORIZED,
                request.getRequestURI(),
                MDC.get("traceId"),
                LocalDateTime.now()
        );

        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}