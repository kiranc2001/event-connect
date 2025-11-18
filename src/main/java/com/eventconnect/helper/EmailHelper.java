package com.eventconnect.helper;

import com.eventconnect.model.Event;
import com.eventconnect.model.Participant;
import com.eventconnect.model.User;
import com.eventconnect.repository.EventRepository;
import com.eventconnect.repository.ParticipantRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class EmailHelper {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ParticipantRepository participantRepository;  // Inject

    @Autowired
    private EventRepository eventRepository;

    public void sendOtpEmail(User user, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Event Connect - OTP for Password Reset");
        message.setText("Your OTP is: " + otp + ". It expires in 10 minutes.");
        mailSender.send(message);
    }

    public void sendWelcomeEmail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Welcome to Event Connect!");
        message.setText("Hi " + user.getName() + ", thanks for signing up!");
        mailSender.send(message);
    }

    public void sendReminderEmail(Event event, User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Event Reminder: " + event.getTitle());
        message.setText("Reminder: " + event.getTitle() + " starts at " + event.getDate() + ". See you there!");
        mailSender.send(message);
    }

    // Scheduled task for reminders 1 hour before event
    @Scheduled(fixedRate = 60000)  // Check every minute
    @Transactional
    public void sendBatchReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderTime = now.plusHours(1);
        List<Event> events = eventRepository.findByDateBetween(now, reminderTime);  // Add this query to repo if needed
        for (Event event : events) {
            List<Participant> participants = participantRepository.findByEventId(event.getId());
            for (Participant p : participants) {
                sendReminderEmail(event, p.getUser());
            }
        }
    }
}