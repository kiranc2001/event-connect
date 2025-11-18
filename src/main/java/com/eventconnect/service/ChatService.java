package com.eventconnect.service;

import com.eventconnect.dto.MessageDto;
import com.eventconnect.model.Message;
import java.util.List;

public interface ChatService {
    void sendMessage(Long eventId, Long userId, MessageDto dto);
    List<Message> getMessages(Long eventId);
}