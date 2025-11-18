package com.eventconnect.helper;

import java.security.SecureRandom;
import java.util.Random;

public class OtpGenerator {
    private static final String NUMBERS = "0123456789";
    private static final int OTP_LENGTH = 6;

    public static String generateOtp() {
        Random random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }
        return otp.toString();
    }
}