package com.example.EventLime.model;

import lombok.Data;
import lombok.Getter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.EventLime.dto.UserDTO;

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
    @NotBlank(message = "Name cannot be blank")
    private String name;

    // No-args constructor required by JPA/Jackson
  // Convert to DTO
  public UserDTO toDTO() {
      return new UserDTO(this.email, this.id, this.name);
  }

}
