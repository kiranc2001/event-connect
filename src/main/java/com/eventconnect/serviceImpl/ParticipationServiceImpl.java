package com.eventconnect.serviceImpl;

import com.eventconnect.dto.EventResponseDto;
import com.eventconnect.dto.ParticipationDto;
import com.eventconnect.exception.EventNotFoundException;
import com.eventconnect.exception.UserNotFoundException;
import com.eventconnect.exception.ValidationException;
import com.eventconnect.helper.EmailHelper;
import com.eventconnect.model.Event;
import com.eventconnect.model.Participant;
import com.eventconnect.model.User;
import com.eventconnect.repository.EventRepository;
import com.eventconnect.repository.ParticipantRepository;
import com.eventconnect.repository.UserRepository;
import com.eventconnect.service.ParticipationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParticipationServiceImpl implements ParticipationService {

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EmailHelper emailHelper;

    @Override
    @Transactional
    public void joinEvent(ParticipationDto dto, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new ValidationException("User not logged in");
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        Event event = eventRepository.findById(dto.getEventId()).orElseThrow(() -> new EventNotFoundException("Event not found"));

        if (participantRepository.findByUserIdAndEventId(userId, dto.getEventId()).isPresent()) {
            throw new ValidationException("Already joined this event");
        }

        Participant participant = new Participant();
        participant.setUser(user);
        participant.setEvent(event);
        participantRepository.save(participant);

        // Send reminder email
        emailHelper.sendReminderEmail(event, user);
    }

    @Override
    @Transactional
    public void leaveEvent(ParticipationDto dto, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new ValidationException("User not logged in");
        if (!participantRepository.findByUserIdAndEventId(userId, dto.getEventId()).isPresent()) {
            throw new ValidationException("Not joined to this event");
        }
        participantRepository.deleteByUserIdAndEventId(userId, dto.getEventId());
    }

    @Override
    public List<EventResponseDto> getJoinedEvents(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) throw new ValidationException("User not logged in");
        return participantRepository.findByUserId(userId).stream()
                .map(p -> mapToResponse(p.getEvent())).collect(Collectors.toList());
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