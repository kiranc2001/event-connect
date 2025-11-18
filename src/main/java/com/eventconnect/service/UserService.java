package com.eventconnect.service;

import com.eventconnect.dto.UserDto;
import com.eventconnect.dto.LoginDto;
import com.eventconnect.dto.OtpDto;
import com.eventconnect.dto.UserResponseDto;
import jakarta.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    UserResponseDto signup(UserDto dto);
    UserResponseDto login(LoginDto dto, HttpSession session);
    void logout(HttpSession session);
    void sendOtp(String email);
    UserResponseDto resetPassword(String email, OtpDto dto);
    List<UserResponseDto> getAllUsers();  // For admin
    UserResponseDto getCurrentUser(HttpSession session);
}