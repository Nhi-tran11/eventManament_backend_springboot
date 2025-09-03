package com.example.EventLime.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.EventLime.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    List<User> findByEmail(String email);
    // Additional query methods can be defined here
    boolean existsByEmail(String email);
    boolean existsByPassword(String password);
}
