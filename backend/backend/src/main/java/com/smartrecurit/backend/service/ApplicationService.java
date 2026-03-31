package com.smartrecurit.backend.service;

import com.smartrecurit.backend.config.FileStorageConfig;
import com.smartrecurit.backend.dto.ApplicationResponse;
import com.smartrecurit.backend.entity.*;
import com.smartrecurit.backend.enums.ApplicationStatus;
import com.smartrecurit.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final FileStorageConfig fileStorage;

    // Called when a candidate clicks "Quick Apply"
    // Notice it accepts both the form data (jobId) AND the uploaded file
    public ApplicationResponse apply(Long jobId, Long candidateId, MultipartFile resumeFile) {

        // Rule: a candidate should not apply for the same job twice
        applicationRepository.findByCandidateIdAndJobId(candidateId, jobId)
                .ifPresent(existing -> {
                    throw new RuntimeException("You have already applied for this job");
                });

        // Load the related entities from DB
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        User candidate = userRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        // Save the resume file to disk and get back the file path
        String savedPath = fileStorage.saveFile(resumeFile);

        // Build and save the application record
        Application application = Application.builder()
                .job(job)
                .candidate(candidate)
                .resumePath(savedPath)
                .build();
        // @PrePersist will set status = APPLIED and appliedAt = now()

        Application saved = applicationRepository.save(application);
        return toResponse(saved);
    }

    // HR uses this to see all applications for a specific job
    public List<ApplicationResponse> getApplicationsByJob(Long jobId) {
        return applicationRepository.findByJobId(jobId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Candidate uses this to track all their own applications
    public List<ApplicationResponse> getMyApplications(Long candidateId) {
        return applicationRepository.findByCandidateId(candidateId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Helper to convert entity → DTO
    private ApplicationResponse toResponse(Application app) {
        return ApplicationResponse.builder()
                .id(app.getId())
                .jobTitle(app.getJob().getTitle())
                .candidateName(app.getCandidate().getName())
                .status(app.getStatus().name())
                .resumePath(app.getResumePath())
                .appliedAt(app.getAppliedAt())
                .build();
    }
}