package com.smartrecurit.backend.dto;

import lombok.Getter;
import lombok.Setter;

// This is what arrives in the request body when HR creates a new job
// We use a separate class so we control exactly what fields are accepted
@Getter @Setter
public class JobRequest {
    private String title;
    private String description;
    private String skillsRequired;       // e.g. "Java, React, PostgreSQL"
    private Integer yearsOfExperience;   // minimum years required
}