package com.some.micro.services;

import com.some.micro.exceptions.UserNotFoundException;
import com.some.micro.model.entities.User;
import com.some.micro.repository.OrdersRepository;
import com.some.micro.repository.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final Logger log = Logger.getLogger(UserService.class.getName());

    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public List<User> getAllUsers() {
        return usersRepository.findAll();
    }

    @Transactional
    public ResponseEntity<String> deleteUserById(Long id) {
        usersRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id: " + id + "doesn't exist"));
        usersRepository.deleteById(id);
        return ResponseEntity.ok().body("Deleted user with id: " + id);
    }
}
