package com.example.EventLime.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Document(collection = "events")
public class Event {
    @Id
    private String id;
    @Setter @Getter
    @NotNull
    private String title;
    @NotNull
    @Setter @Getter
    private String location;
    @NotNull
    @Setter @Getter
    private LocalDate  date;
    @NotNull
    @Setter @Getter
    private LocalTime time;
    @Setter @Getter
    @NotNull
    private double price;
    @NotNull
    @Setter @Getter
    private String description;
    @NotNull
    @Setter @Getter
    private String category;
    @NotNull
    @Setter @Getter
    private int attendees;
    @NotNull
    @Setter @Getter
    private String organizer;
    @NotNull
    @Setter @Getter
    private String createdBy;

    private String imageUrl;  // URL for cloud storage or web access

    public Event(){
        this.attendees = 0; // Default to 0 attendees
        this.title = ""; // Default to empty title
        this.location = ""; // Default to empty location
        this.date = LocalDate.now(); // Default to current date
        this.organizer = ""; // Default to empty organizer
        this.description = ""; // Default to empty description
        this.category = ""; // Default to empty category
        this.price = 0.0; // Default to free event
        this.createdBy = ""; // Default to empty createdBy
        this.imageUrl = ""; // Default to empty imageUrl
    }

}