package com.smartrecurit.backend.dto;

import lombok.Getter;
import lombok.Setter;

// dto/LoginRequest.java
@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}

