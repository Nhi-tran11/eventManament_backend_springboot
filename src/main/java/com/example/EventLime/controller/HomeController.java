package com.example.EventLime.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.example.EventLime.model.Event;
import com.example.EventLime.model.User;
import com.example.EventLime.repository.EventRepository;
import com.example.EventLime.repository.UserRepository;
import com.example.EventLime.uploads.FileStorageService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin(origins = "http://localhost:3000")
@RestController


public class HomeController {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EventRepository eventRepository;
    
    @Autowired
    private FileStorageService fileStorageService;

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
    public ResponseEntity<Map<String, String>> login(@RequestBody User user, HttpServletRequest request){
       Optional<User> loginUser = userRepository.findByEmail(user.getEmail());
        System.out.println("Attempting login for email: " + user.getEmail());
        if(loginUser.isPresent()) {
            if(loginUser.get().getPassword().equals(user.getPassword())) {
                HttpSession session = request.getSession();
                session.setAttribute("user", loginUser.get());
                System.out.println("Session ID: " + session.getId());
             System.out.println("User stored: " + session.getAttribute("user"));
                return ResponseEntity.ok(Map.of("message", "Login successful"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid password"));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Email not found"));
    }
    
    @PostMapping(value ="/event_create", produces = "application/json")
    public ResponseEntity<Map<String, String>> createEvent(@Valid @RequestBody Event event) {
        
        if(eventRepository.existsByTitle(event.getTitle())) {
            if(eventRepository.existsByDate(event.getDate())) {
                if(eventRepository.existsByLocation(event.getLocation())) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Event already exists at this location on this date"));
                }
                eventRepository.save(event);
                return ResponseEntity.ok().body(Map.of("message", "Event created successfully"));
            }
            return ResponseEntity.badRequest().body(Map.of("message", "Event title already exists"));
        }
        eventRepository.save(event);
        return ResponseEntity.ok().body(Map.of("message", "Event created successfully"));
    }

    @PostMapping(value = "/event_create_with_image", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<Map<String, String>> createEventWithImage(@RequestBody Event event) {
        
        try {
             if(eventRepository.existsByTitle(event.getTitle())) {
                if(eventRepository.existsByDate(event.getDate())) {
                    if(eventRepository.existsByLocation(event.getLocation())) {
                        return ResponseEntity.badRequest().body(Map.of("message", "Event already exists at this location on this date"));
                    }
                    eventRepository.save(event);
                    return ResponseEntity.ok().body(Map.of("message", "Event created successfully"));
                }
                
            eventRepository.save(event);
            return ResponseEntity.ok().body(Map.of("message", "Event created successfully with image"));
             }
             eventRepository.save(event);
            return ResponseEntity.ok().body(Map.of("message", "Event created successfully with image"));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error creating event: " + e.getMessage()));
        }
    }
    @GetMapping(value="/current_user", produces="application/json")
    public ResponseEntity<Map<String, Object>> getUserSession(HttpSession session) {
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                System.out.println("Current user ID: " + user.getId());
                return ResponseEntity.ok(Map.of("id", user.getId(), "email", user.getEmail()));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No user logged in"));
    }


    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam(value = "file", required = false) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "No file provided"));
        }
        
        try {
            String filePath = fileStorageService.saveFile(file);
            return ResponseEntity.ok(Map.of("message", "File uploaded successfully", "filePath", filePath));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Could not upload file: " + e.getMessage()));
        }
    }
}
    
