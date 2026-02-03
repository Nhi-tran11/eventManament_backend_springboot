package com.example.EventLime.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EventLime.model.User;
import com.example.EventLime.repository.UserRepository;

@Service
public class UserService{
    @Autowired
    private UserRepository userRepository;
    public Optional<User>findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }
    public boolean existsByEmail (String email)
    {
        return userRepository.existsByEmail(email);
    }
    public boolean existsByPassword (String password)
    {
        return userRepository.existsByPassword(password);
    }
    public User saveUser (User user)
    {
        return userRepository.save(user);
    }

}