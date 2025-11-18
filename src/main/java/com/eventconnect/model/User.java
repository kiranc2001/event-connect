package com.eventconnect.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String passwordHash;  // Use BCrypt in service

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    // For OTP reset
    private String otpCode;
    private LocalDateTime otpExpiry;

    // Timestamps
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}