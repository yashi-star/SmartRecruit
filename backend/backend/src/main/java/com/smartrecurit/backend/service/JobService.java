package com.smartrecurit.backend.service;


import com.smartrecurit.backend.dto.*;
import com.smartrecurit.backend.entity.*;
import com.smartrecurit.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    // HR creates a job — we need the HR's userId so we can record who posted it
    public JobResponse createJob(JobRequest request, Long hrUserId) {
        // First, load the HR user from the database
        User hr = userRepository.findById(hrUserId)
                .orElseThrow(() -> new RuntimeException("HR user not found"));

        // Build the Job entity using the builder pattern Lombok gave us
        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .skillsRequired(request.getSkillsRequired())
                .yearsOfExperience(request.getYearsOfExperience())
                .createdBy(hr)
                .build();
        // @PrePersist in the entity will set postedDate automatically

        Job savedJob = jobRepository.save(job);
        return toResponse(savedJob);  // never return the raw entity
    }

    // Returns all jobs — used by candidates to browse, and HR to manage
    public List<JobResponse> getAllJobs() {
        return jobRepository.findAllByOrderByPostedDateDesc()
                .stream()
                .map(this::toResponse)  // convert each Job entity to JobResponse DTO
                .collect(Collectors.toList());
    }

    // Returns a single job's details — for the job detail page
    public JobResponse getJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + jobId));
        return toResponse(job);
    }

    // Only HR can delete — we validate in the controller via @PreAuthorize
    public void deleteJob(Long jobId) {
        if (!jobRepository.existsById(jobId)) {
            throw new RuntimeException("Job not found with id: " + jobId);
        }
        jobRepository.deleteById(jobId);
    }

    // Update an existing job listing
    public JobResponse updateJob(Long jobId, JobRequest request) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        // Only update fields that were provided
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setSkillsRequired(request.getSkillsRequired());
        job.setYearsOfExperience(request.getYearsOfExperience());

        return toResponse(jobRepository.save(job));
    }

    // Private helper — converts a Job entity into a safe JobResponse DTO
    // This is called a "mapper" — in larger projects you'd use MapStruct library for this
    private JobResponse toResponse(Job job) {
        return JobResponse.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .skillsRequired(job.getSkillsRequired())
                .yearsOfExperience(job.getYearsOfExperience())
                .postedDate(job.getPostedDate())
                .postedByName(job.getCreatedBy().getName())
                .build();
    }
}
