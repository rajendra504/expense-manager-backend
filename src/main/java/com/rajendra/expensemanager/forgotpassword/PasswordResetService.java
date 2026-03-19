package com.rajendra.expensemanager.forgotpassword;

import com.rajendra.expensemanager.common.email.EmailService;
import com.rajendra.expensemanager.exception.ApiException;
import com.rajendra.expensemanager.forgotpassword.dto.ForgotPasswordRequest;
import com.rajendra.expensemanager.forgotpassword.dto.VerifyOtpRequest;
import com.rajendra.expensemanager.user.User;
import com.rajendra.expensemanager.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
public class PasswordResetService {

    private final OtpTokenRepository otpRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(OtpTokenRepository otpRepository,
                                UserRepository userRepository,
                                EmailService emailService,
                                PasswordEncoder passwordEncoder) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendOtp(ForgotPasswordRequest request) {
        // Check user exists
        userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("No account found with this email"));

        // Delete old OTPs for this email
        otpRepository.deleteAllByEmail(request.getEmail());

        // Generate 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Save OTP — expires in 10 minutes
        OtpToken token = new OtpToken();
        token.setEmail(request.getEmail());
        token.setOtp(otp);
        token.setExpiresAt(Instant.now().plusSeconds(600));
        token.setUsed(false);
        otpRepository.save(token);

        // Send email
        emailService.sendOtpEmail(request.getEmail(), otp);
    }

    public void resetPassword(VerifyOtpRequest request) {
        OtpToken token = otpRepository
                .findTopByEmailOrderByCreatedAtDesc(request.getEmail())
                .orElseThrow(() -> new ApiException("OTP not found. Please request a new one"));

        if (token.isUsed()) {
            throw new ApiException("OTP has already been used");
        }

        if (Instant.now().isAfter(token.getExpiresAt())) {
            throw new ApiException("OTP has expired. Please request a new one");
        }

        if (!token.getOtp().equals(request.getOtp())) {
            throw new ApiException("Invalid OTP. Please check and try again");
        }

        // Update password
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Mark OTP as used
        token.setUsed(true);
        otpRepository.save(token);
    }
}
