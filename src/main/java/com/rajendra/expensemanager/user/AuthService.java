package com.rajendra.expensemanager.user;

import com.rajendra.expensemanager.common.email.EmailService;
import com.rajendra.expensemanager.exception.ApiException;
import com.rajendra.expensemanager.security.JwtUtil;
import com.rajendra.expensemanager.user.dto.LoginRequest;
import com.rajendra.expensemanager.user.dto.LoginResponse;
import com.rajendra.expensemanager.user.dto.RegisterRequest;
import com.rajendra.expensemanager.user.dto.RegisterResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class AuthService {

    private static final Logger logger =
            LoggerFactory.getLogger(AuthService.class);

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthService(JwtUtil jwtUtil,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       EmailService emailService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public RegisterResponse register(RegisterRequest request) {

        logger.info("Attempting to register user with email: {}", request.getEmail());

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            logger.warn("Registration failed. Email already exists: {}", request.getEmail());
            throw new ApiException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
        emailService.sendWelcomeEmail(request.getEmail());
        logger.info("User registered successfully: {}", user.getEmail());

        return new RegisterResponse(user.getEmail());
    }

    public LoginResponse login(LoginRequest request) {
        logger.info("Login attempt for email: {}", request.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    logger.error("Login failed. User not found: {}", request.getEmail());
                    return new ApiException("User not found");
                });

        String token = jwtUtil.generateToken(user.getEmail());

        logger.info("Login successful for user: {}", user.getEmail());

        return new LoginResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
}

