package com.eventconnect.controller;

import com.eventconnect.dto.EventDto;
import com.eventconnect.dto.EventResponseDto;
import com.eventconnect.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/events")
//@CrossOrigin("*")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<EventResponseDto>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(eventService.getEventsByCategory(category));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<EventResponseDto>> getUpcoming() {
        return ResponseEntity.ok(eventService.getUpcomingEvents());
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventResponseDto>> search(@RequestParam String q) {
        return ResponseEntity.ok(eventService.searchEvents(q));
    }

    @PostMapping
    public ResponseEntity<EventResponseDto> create(@Valid @RequestBody EventDto dto, HttpSession session) {
        return ResponseEntity.ok(eventService.createEvent(dto, session));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> update(@PathVariable Long id, @Valid @RequestBody EventDto dto, HttpSession session) {
        return ResponseEntity.ok(eventService.updateEvent(id, dto, session));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, HttpSession session) {
        eventService.deleteEvent(id, session);
        return ResponseEntity.ok("Event deleted");
    }

    @GetMapping("/my")
    public ResponseEntity<List<EventResponseDto>> getMyEvents(HttpSession session) {
        return ResponseEntity.ok(eventService.getMyEvents(session));
    }
}