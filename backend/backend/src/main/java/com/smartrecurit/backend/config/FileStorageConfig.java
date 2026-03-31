package com.smartrecurit.backend.config;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

// This class has one job: take a file from the request and save it to disk
// Think of it as a filing clerk — you hand them the resume, they find a drawer and store it
@Component
public class FileStorageConfig {

    // This folder will be created in your project root — make sure it's in .gitignore
    private final Path storageLocation = Paths.get("uploads");

    // Called once when the app starts — creates the uploads folder if it doesn't exist
    public FileStorageConfig() {
        try {
            Files.createDirectories(storageLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not create uploads folder", e);
        }
    }

    // Saves the file and returns the path as a string (which we store in the DB)
    public String saveFile(MultipartFile file) {
        try {
            // UUID gives each file a unique name so two "resume.pdf" files never collide
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path targetPath = storageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toString();  // store this string in the applications table
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }
}
