package com.eventconnect.serviceImpl;

import com.eventconnect.repository.EventRepository;
import com.eventconnect.repository.UserRepository;
import com.eventconnect.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Map<String, Object> getAnalytics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.count());
        stats.put("totalEvents", eventRepository.count());
        stats.put("upcomingEvents", eventRepository.findByDateAfter(LocalDateTime.now()).size());
        // Add more, e.g., categories count via custom query
        return stats;
    }
}