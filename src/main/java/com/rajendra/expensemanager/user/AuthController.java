package com.rajendra.expensemanager.user;

import com.rajendra.expensemanager.exception.ApiResponse;
import com.rajendra.expensemanager.forgotpassword.PasswordResetService;
import com.rajendra.expensemanager.forgotpassword.dto.ForgotPasswordRequest;
import com.rajendra.expensemanager.forgotpassword.dto.VerifyOtpRequest;
import com.rajendra.expensemanager.user.dto.LoginRequest;
import com.rajendra.expensemanager.user.dto.LoginResponse;
import com.rajendra.expensemanager.user.dto.RegisterRequest;
import com.rajendra.expensemanager.user.dto.RegisterResponse;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService,
                          PasswordResetService passwordResetService){
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {

        RegisterResponse response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        true,
                        "User registered successfully",
                        response,
                        null,
                        httpRequest.getRequestURI()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Login successful",
                        response,
                        null,
                        httpRequest.getRequestURI()
                )
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request,
            HttpServletRequest httpRequest) {

        passwordResetService.sendOtp(request);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "OTP sent to your email",
                null, null,
                httpRequest.getRequestURI()
        ));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody VerifyOtpRequest request,
            HttpServletRequest httpRequest) {

        passwordResetService.resetPassword(request);
        return ResponseEntity.ok(new ApiResponse<>(
                true,
                "Password reset successfully",
                null, null,
                httpRequest.getRequestURI()
        ));
    }
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("ok");
    }
}