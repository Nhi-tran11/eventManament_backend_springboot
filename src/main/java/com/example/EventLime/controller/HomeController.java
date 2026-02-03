package com.example.EventLime.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import com.example.EventLime.Service.EventService;
import com.example.EventLime.Service.UserService;
import com.example.EventLime.dto.UserDTO;
import com.example.EventLime.model.AttendeeInformation;
import com.example.EventLime.model.Event;
import com.example.EventLime.model.User;
import com.example.EventLime.repository.AttendeeInformationRepository;
import com.example.EventLime.repository.EventRepository;
import com.example.EventLime.uploads.FileStorageService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController


public class HomeController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private FileStorageService fileStorageService;
    private int quantity;

    @Autowired
    private AttendeeInformationRepository attendeeInformationRepository;

@PostMapping(value = "/signup", produces = "application/json")
public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody User user) {
    // Check if user already exists using findByEmail
    if(userService.existsByEmail(user.getEmail())) {
        return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
    }
    if(user.getPassword() == null || user.getPassword().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
    }
   if(user.getName() == null || user.getName().isEmpty()) {
        return ResponseEntity.badRequest().body(Map.of("message", "Name is required"));
    }
    // Logic to handle signup
    userService.saveUser(user);
    System.out.println("User registered successfully: " + user.getEmail());
    return ResponseEntity.ok().body(Map.of("message", "User registered successfully"));
}
    
    @PostMapping(value = "/login", produces ="application/json")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody User user, HttpServletRequest request){
       Optional<User> loginUser = userService.findByEmail(user.getEmail());
        System.out.println("Attempting login for email: " + user.getEmail());
        if(loginUser.isPresent()) {
            if(loginUser.get().getPassword().equals(user.getPassword())) {
                User foundUser = loginUser.get();

                //Create DTO (no password)
                UserDTO userDTO = new UserDTO(foundUser.getId(), foundUser.getEmail(), foundUser.getName());
                HttpSession session = request.getSession();
                session.setAttribute("user", userDTO);
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
        
        if(eventService.existsByTitle(event.getTitle())) {
            if(eventService.existsByDate(event.getDate())) {
                if(eventService.existsByLocation(event.getLocation())) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Event already exists at this location on this date"));
                }
                eventService.saveEvent(event);
                return ResponseEntity.ok().body(Map.of("message", "Event created successfully"));
            }
            return ResponseEntity.badRequest().body(Map.of("message", "Event title already exists"));
        }
        eventService.saveEvent(event);
        return ResponseEntity.ok().body(Map.of("message", "Event created successfully"));
    }

    @PostMapping(value = "/event_create_with_image", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<Map<String, String>> createEventWithImage(@RequestBody Event event) {
        
        try {
             if(eventService.existsByTitle(event.getTitle())) {
                if(eventService.existsByDate(event.getDate())) {
                    if(eventService.existsByLocation(event.getLocation())) {
                        return ResponseEntity.badRequest().body(Map.of("message", "Event already exists at this location on this date"));
                    }
                    eventService.saveEvent(event);
                    return ResponseEntity.ok().body(Map.of("message", "Event created successfully"));
                }
                
            eventService.saveEvent(event);
            return ResponseEntity.ok().body(Map.of("message", "Event created successfully with image"));
             }
             eventService.saveEvent(event);
            return ResponseEntity.ok().body(Map.of("message", "Event created successfully with image"));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error creating event: " + e.getMessage()));
        }
    }
    @GetMapping(value="/current_user", produces="application/json")
    public ResponseEntity<Map<String, Object>> getUserSession(HttpSession session) {
        if(session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "No user logged in"));
        }
    
        if (session != null) {
            UserDTO user = (UserDTO) session.getAttribute("user");
            if (user != null) {
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
    @GetMapping(value="/events", produces="application/json")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.findAll();
        System.out.println("Retrieved events: " + events.size());
        events.forEach(event -> System.out.println("Event:"+event.getTitle()));
        return ResponseEntity.ok(events);
    }
    @PostMapping(value="/events_by_category", produces="application/json")
    public ResponseEntity<List<Event>> getEventsByCategory(@RequestParam String category) {
        List<Event> events = eventService.findByCategory(category);
        return ResponseEntity.ok(events);
    }
    @PostMapping(value="/book_event_alert", produces="application/json")
    public ResponseEntity<Map<String, Object>> bookEvent(@RequestParam String id) {
        Optional<Event> event= eventService.findById(id);
        if(event.isPresent()){
            quantity=event.get().getQuantity();
            if(quantity==0)
            {
                return ResponseEntity.badRequest().body(Map.of("Event is fully booked",  quantity));
            }
            else{
                return ResponseEntity.ok(Map.of( "message", "Event is available", "quantity", quantity));
            }
        }


        return ResponseEntity.badRequest().body(Map.of("message", "Event not found"));
    }
    @PostMapping(value="/bookedEvent", produces="application/json")
    public synchronized ResponseEntity<Map<String, Object>> bookedEvent(@Valid @RequestBody AttendeeInformation attendeeInformation)
    {
        // Validate input
        if(attendeeInformation.getUserName() == null || attendeeInformation.getUserName().isEmpty() ||
           attendeeInformation.getPhoneNumber() == null || attendeeInformation.getPhoneNumber().isEmpty()||
              attendeeInformation.getTicketsBooked() == 0 
           ) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid attendee information"));
        }
        
        // Check for idempotency key - if this booking was already processed, return the existing booking
        if(attendeeInformation.getIdempotencyKey() != null && !attendeeInformation.getIdempotencyKey().isEmpty()) {
            Optional<AttendeeInformation> existingBooking = attendeeInformationRepository.findByIdempotencyKey(attendeeInformation.getIdempotencyKey());
            if(existingBooking.isPresent()) {
                return ResponseEntity.ok(Map.of(
                    "message", "Booking already exists", 
                    "bookingId", existingBooking.get().getId(),
                    "ticketsBooked", existingBooking.get().getTicketsBooked()
                ));
            }
        }
        
        Optional<Event> event = eventService.findById(attendeeInformation.getEvent().getId());
        if (event.isPresent()) {
            if (event.get().getQuantity() >= attendeeInformation.getTicketsBooked()) {
                event.get().setQuantity(event.get().getQuantity() - attendeeInformation.getTicketsBooked());
                eventService.saveEvent(event.get());
                AttendeeInformation savedBooking = attendeeInformationRepository.save(attendeeInformation);
                return ResponseEntity.ok(Map.of(
                    "message", "Event booked successfully", 
                    "bookingId", savedBooking.getId(),
                    "quantity is left", event.get().getQuantity()
                ));
            }
            else{
                return ResponseEntity.badRequest().body(Map.of("message", "Not enough tickets available", "quantity left", event.get().getQuantity()));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Event is not found"));
    }
    @PostMapping(value="eventBooked_information", produces="application/json")
    public ResponseEntity<Map<String, Object>> getAllBookedEvent(@RequestParam String userId)
    {
        List<AttendeeInformation> attendeeInformation = attendeeInformationRepository.findByUserId(userId);
        if(attendeeInformation.isEmpty())
        {
            return ResponseEntity.badRequest().body(Map.of("message", "No booked events found for this user"));
        }
    
        return ResponseEntity.ok(Map.of("bookedEvents", attendeeInformation));
    }

    @PostMapping(value="/logout", produces="application/json")
    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
        if(session !=null) {
            session.invalidate();
            return ResponseEntity.ok(Map.of("message", "Logout successful"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "No active session found"));
    }
    @PostMapping(value="event_management", produces="application/json")
    public ResponseEntity<List<Event>> getEventsByUserId (@RequestParam String userId){
        List<Event> events = eventService.findByCreatedBy(userId);
        if(events.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        return ResponseEntity.ok(events);
    }
    @PostMapping(value="/attendee_information", produces="application/json")
    public ResponseEntity<List<AttendeeInformation>>getAttendeeInformation (@RequestParam String eventCreatedBy){
        List <AttendeeInformation> attendeeInformation = attendeeInformationRepository.findByEventCreatedBy(eventCreatedBy);
        if(attendeeInformation.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }

        return ResponseEntity.ok(attendeeInformation);
    }
}

