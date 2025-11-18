package com.eventconnect.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EventResponseDto {
    private Long id;
    private String title;
    private String description;
    private String category;
    private LocalDateTime date;
    private String location;
    private Long createdById;
    private LocalDateTime createdAt;
}