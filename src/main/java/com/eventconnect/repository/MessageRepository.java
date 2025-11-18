package com.eventconnect.repository;

import com.eventconnect.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByEventIdOrderByTimestampAsc(Long eventId);
}