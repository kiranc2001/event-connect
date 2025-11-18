package com.eventconnect.service;

import com.eventconnect.dto.EventDto;
import com.eventconnect.dto.EventResponseDto;
import jakarta.servlet.http.HttpSession;
import java.util.List;

public interface EventService {
    List<EventResponseDto> getAllEvents();
    List<EventResponseDto> getEventsByCategory(String category);
    List<EventResponseDto> getUpcomingEvents();
    List<EventResponseDto> searchEvents(String query);
    EventResponseDto createEvent(EventDto dto, HttpSession session);
    EventResponseDto getEventById(Long id);
    EventResponseDto updateEvent(Long id, EventDto dto, HttpSession session);
    void deleteEvent(Long id, HttpSession session);
    List<EventResponseDto> getMyEvents(HttpSession session);  // Created + joined
}