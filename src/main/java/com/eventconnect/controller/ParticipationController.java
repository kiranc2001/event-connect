package com.eventconnect.controller;

import com.eventconnect.dto.EventResponseDto;
import com.eventconnect.dto.ParticipationDto;
import com.eventconnect.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/participations")
//@CrossOrigin("*")
public class ParticipationController {

    @Autowired
    private ParticipationService participationService;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody ParticipationDto dto, HttpSession session) {
        participationService.joinEvent(dto, session);
        return ResponseEntity.ok("Joined successfully");
    }

    @PostMapping("/leave")
    public ResponseEntity<String> leave(@RequestBody ParticipationDto dto, HttpSession session) {
        participationService.leaveEvent(dto, session);
        return ResponseEntity.ok("Left successfully");
    }

    @GetMapping("/joined")
    public ResponseEntity<List<EventResponseDto>> getJoined(HttpSession session) {
        return ResponseEntity.ok(participationService.getJoinedEvents(session));
    }
}