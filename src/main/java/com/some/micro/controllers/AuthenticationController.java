package com.some.micro.controllers;


import com.some.micro.mappers.UserMapper;
import com.some.micro.model.dto.AuthenticationResponseDto;
import com.some.micro.model.dto.LoginRequestDto;
import com.some.micro.model.dto.RegistrationRequestDto;
import com.some.micro.model.dto.UserResponseDto;
import com.some.micro.services.UserService;
import com.some.micro.services.impl.AuthenticationServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me() {
        return ResponseEntity.ok().body(authenticationService.getInfoAboutUser());
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegistrationRequestDto registrationDto) {

        if (userService.existsByUsername(registrationDto.getUsername())) {
            return ResponseEntity.badRequest().body("Имя пользователя уже занято");
        }

        authenticationService.register(registrationDto);

        return ResponseEntity.ok("Регистрация прошла успешно");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {

        return authenticationService.refreshToken(request, response);
    }
}