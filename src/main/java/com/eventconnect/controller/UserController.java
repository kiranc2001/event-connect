package com.eventconnect.controller;

import com.eventconnect.dto.*;
import com.eventconnect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserDto dto) {
        return ResponseEntity.ok(userService.signup(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody LoginDto dto, HttpSession session) {
        return ResponseEntity.ok(userService.login(dto, session));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        userService.logout(session);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        userService.sendOtp(email);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PutMapping("/reset-password/{email}")
    public ResponseEntity<UserResponseDto> resetPassword(@PathVariable String email, @Valid @RequestBody OtpDto dto) {
        return ResponseEntity.ok(userService.resetPassword(email, dto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(HttpSession session) {
        return ResponseEntity.ok(userService.getCurrentUser(session));
    }
}