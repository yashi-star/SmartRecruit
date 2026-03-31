package com.smartrecurit.backend.dto;

import lombok.*;
import java.time.LocalDate;

// This is what we return — notice it has postedDate and hrName
// which aren't in the request. We compute them on the server.
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class JobResponse {
    private Long id;
    private String title;
    private String description;
    private String skillsRequired;
    private Integer yearsOfExperience;
    private LocalDate postedDate;
    private String postedByName;  // so the candidate can see who posted it
}