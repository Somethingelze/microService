package com.some.micro.services.impl;

import com.some.micro.mappers.UserMapper;
import com.some.micro.model.dto.AuthenticationResponseDto;
import com.some.micro.model.dto.LoginRequestDto;
import com.some.micro.model.dto.RegistrationRequestDto;
import com.some.micro.model.dto.UserResponseDto;
import com.some.micro.model.entities.TokenEntity;
import com.some.micro.model.entities.UserEntity;
import com.some.micro.model.enums.Role;
import com.some.micro.repository.TokenRepository;
import com.some.micro.repository.UserRepository;
import com.some.micro.services.AuthentificationService;
import com.some.micro.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AuthenticationServiceImpl implements AuthentificationService {

    UserRepository userRepository;
    JwtService jwtService;
    PasswordEncoder passwordEncoder;
    AuthenticationManager authenticationManager;
    TokenRepository tokenRepository;
    private final UserMapper userMapper;

    public void register(RegistrationRequestDto request) {
        log.info("Register request: {}", request);

        UserEntity user = new UserEntity();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    private void revokeAllToken(UserEntity user) {
        log.info("Revoking all tokens for user {}", user.getUsername());

        List<TokenEntity> validTokens = tokenRepository.findAllByUserIdAndLoggedOutFalse(user.getId());

        if(!validTokens.isEmpty()){
            validTokens.forEach(t ->{
                t.setLoggedOut(true);
            });
        }

        tokenRepository.saveAll(validTokens);
    }

    private void saveUserToken(String accessToken, String refreshToken, UserEntity user) {

        log.info("Saving user token for user {}", user.getUsername());

        TokenEntity token = new TokenEntity();

        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);

        tokenRepository.save(token);
    }

    public AuthenticationResponseDto authenticate(LoginRequestDto request) {

        log.info("Authenticate request: {}", request);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User with name: " + request.getUsername() + " not found"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllToken(user);

        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponseDto(accessToken, refreshToken);
    }

    public ResponseEntity<AuthenticationResponseDto> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7);
        String username = jwtService.extractUsername(token);

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with name: " + username + " not found"));

        if (jwtService.isValidRefresh(token, user)) {

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllToken(user);

            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity<>(new AuthenticationResponseDto(accessToken, refreshToken), HttpStatus.OK);

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public UserResponseDto getInfoAboutUser()   {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserEntity> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
        return userMapper.toUserResponseDto(user.get());
        } else {
            throw new UsernameNotFoundException("User with name: " + username + " not found");
        }
    }
}