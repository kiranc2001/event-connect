package com.eventconnect.serviceImpl;

import com.eventconnect.dto.MessageDto;
import com.eventconnect.exception.EventNotFoundException;
import com.eventconnect.exception.UserNotFoundException;
import com.eventconnect.model.Event;
import com.eventconnect.model.Message;
import com.eventconnect.model.User;
import com.eventconnect.repository.EventRepository;
import com.eventconnect.repository.MessageRepository;
import com.eventconnect.repository.UserRepository;
import com.eventconnect.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;  // For WebSocket broadcast

    @Override
    public void sendMessage(Long eventId, Long userId, MessageDto dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found"));

        Message message = new Message();
        message.setEvent(event);
        message.setUser(user);
        message.setMessage(dto.getMessage());
        Message saved = messageRepository.save(message);

        // Broadcast via WebSocket
        messagingTemplate.convertAndSend("/topic/messages/" + eventId, saved);
    }

    @Override
    public List<Message> getMessages(Long eventId) {
        return messageRepository.findByEventIdOrderByTimestampAsc(eventId);
    }
}