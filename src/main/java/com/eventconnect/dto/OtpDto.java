package com.eventconnect.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OtpDto {
    @NotBlank(message = "OTP is required")
    private String otp;

    @NotBlank(message = "New password is required")
    private String newPassword;
}