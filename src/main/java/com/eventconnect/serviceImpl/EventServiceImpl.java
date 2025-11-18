package com.eventconnect.serviceImpl;

import com.eventconnect.dto.*;
import com.eventconnect.exception.*;
import com.eventconnect.model.Event;
import com.eventconnect.model.Participant;
import com.eventconnect.model.User;
import com.eventconnect.repository.EventRepository;
import com.eventconnect.repository.ParticipantRepository;
import com.eventconnect.repository.UserRepository;
import com.eventconnect.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ParticipantRepository participantRepository;  // For getMyEvents

    @Override
    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<EventResponseDto> getEventsByCategory(String category) {
        return eventRepository.findByCategory(category).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<EventResponseDto> getUpcomingEvents() {
        return eventRepository.findByDateAfter(LocalDateTime.now()).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<EventResponseDto> searchEvents(String query) {
        return eventRepository.searchEvents(query).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public EventResponseDto createEvent(EventDto dto, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new ValidationException("User not logged in");
        User creator = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("Creator not found"));

        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setCategory(dto.getCategory());
        event.setDate(dto.getDate());
        event.setLocation(dto.getLocation());
        event.setCreatedBy(creator);
        Event saved = eventRepository.save(event);
        return mapToResponse(saved);
    }

    @Override
    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found with ID: " + id));
        return mapToResponse(event);
    }

    @Override
    public EventResponseDto updateEvent(Long id, EventDto dto, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new ValidationException("User not logged in");
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found"));
        if (!event.getCreatedBy().getId().equals(userId)) throw new ValidationException("Not authorized to update this event");

        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setCategory(dto.getCategory());
        event.setDate(dto.getDate());
        event.setLocation(dto.getLocation());
        Event updated = eventRepository.save(event);
        return mapToResponse(updated);
    }

    @Override
    public void deleteEvent(Long id, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new ValidationException("User not logged in");
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found"));
        if (!event.getCreatedBy().getId().equals(userId)) throw new ValidationException("Not authorized to delete this event");
        eventRepository.delete(event);
    }

    @Override
    public List<EventResponseDto> getMyEvents(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new ValidationException("User not logged in");
        // Created events
        List<Event> created = eventRepository.findAll().stream()
                .filter(e -> e.getCreatedBy().getId().equals(userId)).collect(Collectors.toList());
        // Joined events via participants
        List<Event> joined = participantRepository.findByUserId(userId).stream()
                .map(Participant::getEvent).collect(Collectors.toList());
        // Combine (allow duplicates if any, or use Set for unique)
        created.addAll(joined);
        return created.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private EventResponseDto mapToResponse(Event event) {
        EventResponseDto response = new EventResponseDto();
        response.setId(event.getId());
        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setCategory(event.getCategory());
        response.setDate(event.getDate());
        response.setLocation(event.getLocation());
        response.setCreatedById(event.getCreatedBy().getId());
        response.setCreatedAt(event.getCreatedAt());
        return response;
    }
}