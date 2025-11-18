package com.eventconnect.repository;

import com.eventconnect.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCategory(String category);
    List<Event> findByDateAfter(LocalDateTime date);
    @Query("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Event> searchEvents(@Param("search") String search);

    // In EventRepository.java
    @Query("SELECT e FROM Event e WHERE e.date BETWEEN :start AND :end")
    List<Event> findByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}