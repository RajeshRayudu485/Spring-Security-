package com.example.demo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Validate user credentials
    public boolean validateUserCredentials(String email, String rawPassword) {
        logger.info("Validating credentials for email: {}", email);

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
            if (matches) {
                logger.info("Credentials valid for email: {}", email);
            } else {
                logger.warn("Invalid password for email: {}", email);
            }
            return matches;
        } else {
            logger.warn("User not found during validation: {}", email);
            return false;
        }
    }

    // Get user by email
    public User getUserByUsername(String email) {
        logger.info("Fetching user by email: {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", email);
                    return new UsernameNotFoundException("User not found");
                });
    }

    // Create user
    public String createUser(User user) {
        logger.info("Creating user with email: {}", user.getEmail());

        if (user.getPassword() == null) {
            logger.error("Password must not be null for email: {}", user.getEmail());
            throw new IllegalArgumentException("Password must not be null");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
        logger.info("User created successfully: {}", user.getEmail());

        return "User created successfully";
    }

    // ✅ Get user by ID
    public User getUserById(Long userId) {
        logger.info("Fetching user by ID: {}", userId);

        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new UsernameNotFoundException("User not found with ID: " + userId);
                });
    }
}
