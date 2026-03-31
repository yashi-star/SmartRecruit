package com.smartrecurit.backend.exception;

import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

// @RestControllerAdvice means "apply this to every controller in the app"
// Think of it as a global catch block for your entire API
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catches RuntimeException from your services (like "Job not found")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        return ResponseEntity.badRequest().body(Map.of(
                "status", 400,
                "message", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString()
        ));
    }

    // Catches 403 errors from @PreAuthorize failures
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of(
                "status", 403,
                "message", "You do not have permission to perform this action",
                "timestamp", LocalDateTime.now().toString()
        ));
    }
}