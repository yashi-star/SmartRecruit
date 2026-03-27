package com.smartrecurit.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// dto/AuthResponse.java
// This is what you send back to the frontend after successful login
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String role;
    private String name;
    private Long userId;
}