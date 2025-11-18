package com.eventconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private String location;  // Address string


    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    private LocalDateTime createdAt = LocalDateTime.now();
}