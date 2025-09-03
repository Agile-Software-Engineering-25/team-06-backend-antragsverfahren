package com.ase.userservice.controllers;

import com.ase.userservice.entities.User;
import com.ase.userservice.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{matriculationNumber}")
    public ResponseEntity<User> getUserByMatriculationNumber(@PathVariable String matriculationNumber) {
        Optional<User> user = userRepository.findByMatriculationNumber(matriculationNumber);
        return user.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
