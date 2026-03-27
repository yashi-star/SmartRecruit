package com.smartrecurit.backend.dto;

import com.smartrecurit.backend.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private UserRole role;   // HR, INTERVIEWER, or CANDIDATE
}
