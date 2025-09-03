package com.example.EventLime.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.example.EventLime.model.User;
import com.example.EventLime.repository.UserRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class HomeController {
    @Autowired
    private UserRepository userRepository;

@PostMapping(value = "/signup", produces = "application/json")
public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody User user) {
    // Check if user already exists using findByEmail
    if(userRepository.existsByEmail(user.getEmail())) {
        return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
    }
    if(user.getPassword() == null || user.getPassword().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
    }
   if(user.getName() == null || user.getName().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("message", "Name is required"));
    }
    // Logic to handle signup
    userRepository.save(user);
    System.out.println("User registered successfully: " + user.getEmail());
    return ResponseEntity.ok().body(Map.of("message", "User registered successfully"));
}
    
    @PostMapping(value = "/login", produces ="application/json")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user){
        
        if(userRepository.existsByEmail(user.getEmail())) {
            if(userRepository.existsByPassword(user.getPassword())) {
                return ResponseEntity.ok(Map.of("message", "Login successful"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid password"));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Email not found"));
    }
    

}