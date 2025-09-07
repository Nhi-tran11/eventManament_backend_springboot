package com.example.EventLime.controller;

import com.example.EventLime.model.User;
import com.example.EventLime.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(properties = {
    "spring.data.mongodb.database=eventlime_test"
})
class HomeControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        // Clean up test data
        userRepository.deleteAll();
    }

    @Test
    void signup_ValidUser_ReturnsCreatedUser() throws Exception {
        User newUser = new User("test@example.com", "password123", null);

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.id").isNotEmpty());

        // Verify user was actually saved to database
        // var savedUsers = userRepository.findByEmail("test@example.com");
        // assert savedUsers.size() == 1;
        // assert savedUsers.get(0).getEmail().equals("test@example.com");
    }

    @Test
    void signup_EmptyEmail_Returns400() throws Exception {
        User invalidUser = new User("", "password123", null);

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void signup_EmptyPassword_Returns400() throws Exception {
        User invalidUser = new User("test@example.com", "", null);

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void signup_InvalidJson_Returns400() throws Exception {
        String invalidJson = "{ \"email\": \"test@example.com\" }"; // missing password

        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void signup_NoContentType_Returns415() throws Exception {
        User newUser = new User("test@example.com", "password123", null);

        mockMvc.perform(post("/signup")
                .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isUnsupportedMediaType());
    }
}
