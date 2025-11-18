package com.eventconnect.service;

import com.eventconnect.dto.EventResponseDto;
import com.eventconnect.dto.ParticipationDto;
import jakarta.servlet.http.HttpSession;
import java.util.List;

public interface ParticipationService {
    void joinEvent(ParticipationDto dto, HttpSession session);
    void leaveEvent(ParticipationDto dto, HttpSession session);
    List<EventResponseDto> getJoinedEvents(HttpSession session);
}