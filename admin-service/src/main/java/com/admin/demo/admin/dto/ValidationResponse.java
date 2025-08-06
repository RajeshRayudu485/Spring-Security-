package com.admin.demo.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ValidationResponse {
    private boolean valid;
    private UserResponse userResponse;
}