package com.rajendra.expensemanager.user;

import com.rajendra.expensemanager.exception.ApiResponse;
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

    public AuthController(AuthService authService){
        this.authService = authService;
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
}