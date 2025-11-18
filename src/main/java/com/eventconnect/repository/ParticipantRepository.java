package com.eventconnect.repository;

import com.eventconnect.model.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findByUserIdAndEventId(Long userId, Long eventId);
    List<Participant> findByUserId(Long userId);
    List<Participant> findByEventId(Long eventId);
    void deleteByUserIdAndEventId(Long userId, Long eventId);
}