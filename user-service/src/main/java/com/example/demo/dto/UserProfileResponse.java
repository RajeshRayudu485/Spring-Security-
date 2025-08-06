package com.example.demo.dto;

import lombok.Data;

@Data
public class UserProfileResponse {
    private Long userId;
    private String email;
    private String name;
    private String role;

    // Getters & Setters
}

