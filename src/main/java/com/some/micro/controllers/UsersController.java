package com.some.micro.controllers;

import com.some.micro.model.dto.UserResponseDto;
import com.some.micro.services.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserServiceImpl userServiceImpl;

    public UsersController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public List<UserResponseDto> getUsers() {
        return userServiceImpl.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userServiceImpl.deleteUserById(id);
    }

}
