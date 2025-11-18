package com.eventconnect.serviceImpl;

import com.eventconnect.dto.*;
import com.eventconnect.exception.*;
import com.eventconnect.helper.EmailHelper;
import com.eventconnect.helper.OtpGenerator;
import com.eventconnect.model.User;
import com.eventconnect.model.Role;
import com.eventconnect.repository.UserRepository;
import com.eventconnect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailHelper emailHelper;

    @Override
    public UserResponseDto signup(UserDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.USER);  // Default
        User saved = userRepository.save(user);
        emailHelper.sendWelcomeEmail(saved);  // Optional
        return mapToResponse(saved);
    }

    @Override
    public UserResponseDto login(LoginDto dto, HttpSession session) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + dto.getEmail()));
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new ValidationException("Invalid password");
        }
        session.setAttribute("userId", user.getId());
        return mapToResponse(user);
    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @Override
    public void sendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        String otp = OtpGenerator.generateOtp();
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);
        emailHelper.sendOtpEmail(user, otp);
    }

    @Override
    public UserResponseDto resetPassword(String email, OtpDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (user.getOtpExpiry() == null || user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("OTP has expired. Request a new one.");
        }
        if (!user.getOtpCode().equals(dto.getOtp())) {
            throw new ValidationException("Invalid OTP");
        }
        user.setPasswordHash(passwordEncoder.encode(dto.getNewPassword()));
        user.setOtpCode(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
        return mapToResponse(user);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            throw new ValidationException("User not logged in");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapToResponse(user);
    }

    private UserResponseDto mapToResponse(User user) {
        UserResponseDto response = new UserResponseDto();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        return response;
    }
}