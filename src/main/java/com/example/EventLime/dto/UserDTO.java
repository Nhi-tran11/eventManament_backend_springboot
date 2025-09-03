package com.example.EventLime.dto;

import lombok.Getter;
import lombok.Setter;

public class UserDTO {
    @Getter @Setter
    private String email;
 

    public UserDTO(String email) {
        this.email = email;
    }

}
