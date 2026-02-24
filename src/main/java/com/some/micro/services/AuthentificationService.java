package com.some.micro.services;

import com.some.micro.model.dto.AuthenticationResponseDto;
import com.some.micro.model.dto.LoginRequestDto;
import com.some.micro.model.dto.RegistrationRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthentificationService {
    void register(RegistrationRequestDto request);
    AuthenticationResponseDto authenticate(LoginRequestDto request);
    ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response);
}
