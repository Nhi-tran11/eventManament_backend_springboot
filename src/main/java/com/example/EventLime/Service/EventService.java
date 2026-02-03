package com.example.EventLime.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.EventLime.model.Event;
import com.example.EventLime.repository.EventRepository;
@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    
    public boolean existsByTitle(String title) {
        return eventRepository.existsByTitle(title);
    }
    public boolean existsByLocation(String location) {
        return eventRepository.existsByLocation(location);
    }
    public boolean existsByDate(java.time.LocalDate date) {
        return eventRepository.existsByDate(date);
    }
    public Event saveEvent(Event event){
        return eventRepository.save(event);
    }
    public List<Event> findAll()
    {
        return eventRepository.findAll();
    }
    public List<Event> findByCategory(String category)
    {
        return eventRepository.findByCategory(category);
    }
    public Optional<Event> findById(String id)
    {
        return eventRepository.findById(id);
    }
    public List<Event> findByCreatedBy(String createdBy)
    {
        return eventRepository.findByCreatedBy(createdBy);
    }
}

