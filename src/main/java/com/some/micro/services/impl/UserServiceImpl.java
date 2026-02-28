package com.some.micro.services.impl;

import com.some.micro.mappers.UserMapper;
import com.some.micro.model.dto.UserResponseDto;
import com.some.micro.model.entities.UserEntity;
import com.some.micro.model.enums.Role;
import com.some.micro.repository.UserRepository;
import com.some.micro.services.UserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    Logger log = Logger.getLogger(UserServiceImpl.class.getName());
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;

    @Override
    public List<UserResponseDto> getAllUsers() {
        log.info("Getting all users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponseDto)
                .toList();
    }


    @Override
    @Transactional
    public void registerUser(String username, String rawPassword) {
        log.info("Registering user: " + username);
        String encodedPassword = passwordEncoder.encode(rawPassword);
        if (encodedPassword != null) {
            userRepository.save(new UserEntity(username, encodedPassword, Role.USER));
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        log.info("Checking if user with username: " + username);
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean existsById(UUID id) {
        log.info("Checking if user with id: " + id);
        return userRepository.findById(id).isPresent();
    }

    @Override
    public void deleteUserById(UUID id) {
        log.info("Deleting user with id: " + id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: " + username);
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with username: " + username +  " doesn't exist"));
    }
}
