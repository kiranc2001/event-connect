package com.eventconnect.controller;

import com.eventconnect.dto.MessageDto;
import com.eventconnect.exception.ValidationException;
import com.eventconnect.model.Message;
import com.eventconnect.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
//@CrossOrigin("*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/messages/{eventId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long eventId) {
        return ResponseEntity.ok(chatService.getMessages(eventId));
    }

    // WebSocket endpoint (handled in config, but controller for REST send if needed)
    @PostMapping("/send/{eventId}")
    public ResponseEntity<String> sendMessage(@PathVariable Long eventId, @RequestBody MessageDto dto, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new ValidationException("User not logged in");
        chatService.sendMessage(eventId, userId, dto);
        return ResponseEntity.ok("Message sent");
    }
}