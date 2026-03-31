package com.smartrecurit.backend.dto;


import lombok.Getter;
import lombok.Setter;

// Candidates send just the job ID they want to apply for
// The resume file is sent separately as a multipart upload
@Getter @Setter
public class ApplicationRequest {
    private Long jobId;
}