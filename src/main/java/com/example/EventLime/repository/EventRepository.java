package com.example.EventLime.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.example.EventLime.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {

    List<Event> findByCategory(String category);
    List<Event> findByOrganizer(String organizer);
    List<Event> findByLocation(String location);
    List<Event> findByDate(LocalDate date);
    List<Event> findByTime(LocalTime time);
    List<Event> findByPrice(double price);
    List<Event> findByAttendees(int attendees);
    List<Event> findByCreatedBy(String createdBy);
    boolean existsByTitle(String title);
    boolean existsByLocation(String location);
    boolean existsByDate(LocalDate date);

}
