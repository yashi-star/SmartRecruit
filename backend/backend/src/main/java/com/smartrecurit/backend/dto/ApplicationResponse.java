package com.smartrecurit.backend.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor @Builder
public class ApplicationResponse {
    private Long id;
    private String jobTitle;
    private String candidateName;
    private String status;
    private String resumePath;
    private LocalDateTime appliedAt;
}