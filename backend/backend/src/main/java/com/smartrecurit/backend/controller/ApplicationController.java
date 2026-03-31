package com.smartrecurit.backend.controller;

import com.smartrecurit.backend.dto.ApplicationResponse;
import com.smartrecurit.backend.repository.UserRepository;
import com.smartrecurit.backend.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final UserRepository userRepository;

    // CANDIDATE ONLY — apply for a job
    // This is a multipart request: one part is the JSON (jobId), other is the file
    @PostMapping("/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationResponse> apply(
            @RequestParam Long jobId,
            @RequestParam MultipartFile resume,       // the uploaded PDF file
            @AuthenticationPrincipal UserDetails userDetails) {

        Long candidateId = userRepository.findByEmail(userDetails.getUsername()).get().getId();
        return ResponseEntity.ok(applicationService.apply(jobId, candidateId, resume));
    }

    // CANDIDATE — see all their own applications and statuses
    @GetMapping("/mine")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<ApplicationResponse>> myApplications(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long candidateId = userRepository.findByEmail(userDetails.getUsername()).get().getId();
        return ResponseEntity.ok(applicationService.getMyApplications(candidateId));
    }

    // HR ONLY — see all applications for a specific job
    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<List<ApplicationResponse>> applicationsByJob(@PathVariable Long jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJob(jobId));
    }
}
