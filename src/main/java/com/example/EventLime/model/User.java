package com.example.EventLime.model;

import lombok.Data;
import lombok.Getter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Data
@Document(collection= "users")
public class User {
    @Id
    private String id;
    @NotNull
    @NotBlank(message = "Email cannot be blank")
    @Getter
    private String email;
    @NotNull
    @NotBlank(message = "Password cannot be blank")
    @Getter
    private String password;
    @NotNull
    @Getter
    private String name;

    // No-args constructor required by JPA/Jackson
    public User() {}

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
