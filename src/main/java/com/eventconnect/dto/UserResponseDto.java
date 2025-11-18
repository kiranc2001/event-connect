package com.eventconnect.dto;

import com.eventconnect.model.Role;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String name;
    private String email;
    private Role role;
}