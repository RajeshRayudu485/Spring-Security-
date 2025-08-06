package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.UserResponse;
import com.example.demo.dto.ValidationResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // 1. Validate API — called by Auth Service
    @PostMapping("/login")
    public ResponseEntity<ValidationResponse> validate(@RequestBody AuthRequest authRequest) {
        logger.info("Validation request received for user: {}", authRequest.getUsername());

        boolean valid = userService.validateUserCredentials(authRequest.getUsername(), authRequest.getPassword());

        if (!valid) {
            logger.warn("Validation failed for user: {}", authRequest.getUsername());
            return ResponseEntity.ok(new ValidationResponse(false, null));
        }

        User user = userService.getUserByUsername(authRequest.getUsername());

        UserResponse userResponse = new UserResponse(user.getUserId(), user.getEmail(), user.getRole());

        logger.info("Validation successful for user: {}, userId: {}, email: {}, role: {}",
                authRequest.getUsername(), user.getUserId(), user.getEmail(), user.getRole());

        return ResponseEntity.ok(new ValidationResponse(true, userResponse));
    }

    // 2. Profile API — called by frontend after login
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@RequestHeader("X-User-Id") Long userId) {
        logger.info("Profile request received for user ID from header: {}", userId);

        User user = userService.getUserById(userId);

        if (user == null) {
            logger.warn("User not found with ID: {}", userId);
            return ResponseEntity.notFound().build();
        }

        logger.info("Returning profile for user ID: {}", userId);
        return ResponseEntity.ok(user);
    }


    @PostMapping("/create-user")
    public String createUser(@RequestBody User user) {
        logger.info("Create user request received for username: {}", user.getEmail());

        String result = userService.createUser(user);

        logger.info("Create user result for username {}: {}", user.getEmail(), result);
        return result;
    }
}
