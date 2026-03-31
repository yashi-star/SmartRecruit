package com.smartrecurit.backend.controller;


import com.smartrecurit.backend.dto.*;
import com.smartrecurit.backend.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.smartrecurit.backend.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // Vite's default port
public class JobController {

    private final JobService jobService;
    private final UserRepository userRepository; // needed to resolve email → userId

    // PUBLIC — candidates (even unauthenticated) can see all job listings
    @GetMapping
    public ResponseEntity<List<JobResponse>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    // PUBLIC — candidates can view a single job's full details
    @GetMapping("/{id}")
    public ResponseEntity<JobResponse> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    // HR ONLY — @PreAuthorize blocks any other role before the method even runs
    // @AuthenticationPrincipal gives us the currently logged-in user's details
    @PostMapping
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<JobResponse> createJob(
            @RequestBody JobRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Resolve the email from the JWT to the actual user ID in the database
        Long hrId = userRepository.findByEmail(userDetails.getUsername()).get().getId();
        return ResponseEntity.ok(jobService.createJob(request, hrId));
    }

    // HR ONLY — update a job listing
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<JobResponse> updateJob(
            @PathVariable Long id,
            @RequestBody JobRequest request) {
        return ResponseEntity.ok(jobService.updateJob(id, request));
    }

    // HR ONLY — remove a job listing
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('HR')")
    public ResponseEntity<String> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok("Job deleted successfully");
    }
}
