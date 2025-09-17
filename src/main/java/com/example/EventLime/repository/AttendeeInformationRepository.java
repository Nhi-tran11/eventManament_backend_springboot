package com.example.EventLime.repository;

import java.util.List;
import java.util.Optional;

import com.example.EventLime.model.AttendeeInformation;
import com.example.EventLime.model.Event;

import org.springframework.data.mongodb.repository.MongoRepository;
public interface AttendeeInformationRepository extends MongoRepository<AttendeeInformation, String> {
    List<AttendeeInformation> findByUserId(String userId);
    Optional<AttendeeInformation> findByEventAndUserId(Event event, String userId);

    List<AttendeeInformation> findByEvent(Event event);
    List<AttendeeInformation> findByEventCreatedBy(String eventCreatedBy);
}
