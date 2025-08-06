package com.example.demo.dto;

import lombok.Data;

@Data
public class ValidationResponse {
    private boolean valid;
    private UserResponse userResponse;
	public ValidationResponse(boolean valid, UserResponse userResponse) {
		super();
		this.valid = valid;
		this.userResponse = userResponse;
	}
    
}