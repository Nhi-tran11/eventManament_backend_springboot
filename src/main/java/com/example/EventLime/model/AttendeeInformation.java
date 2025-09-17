package com.example.EventLime.model;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Document  (collection = "attendeeInformation")
public class AttendeeInformation {
    @NotNull @Getter  @Setter
    private Event event;
    @NotNull @Getter  @Setter
    private String userId;
    @NotNull @Getter  @Setter
    @Min(value = 1, message = "You must book at least 1 ticket")
    private int ticketsBooked;
    @NotBlank (message = "User name is required") @Getter  @Setter
    private String userName;
    @NotBlank (message = "Phone number is required") @Getter  @Setter
    private String phoneNumber;
    private String specialRequest;



    public AttendeeInformation(Event event, String userId, String userName, String phoneNumber, String specialRequest) {
        this.ticketsBooked = 0; // Default to 0 tickets booked
        this.event = event;
        this.userId = userId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.specialRequest = specialRequest;
    }

    
}
