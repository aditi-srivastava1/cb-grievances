package com.cbgrievances.cb_grievances.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");

    // "uploads" folder inside the project root
    private final Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();

    // Saves the photo and returns the new file name, or null if no photo was chosen
    public String store(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalName = file.getOriginalFilename();
        String extension = "";
        if (originalName != null && originalName.contains(".")) {
            extension = originalName.substring(originalName.lastIndexOf('.') + 1).toLowerCase();
        }
        String contentType = file.getContentType();

        if (!ALLOWED_EXTENSIONS.contains(extension)
                || contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only JPG and PNG images are allowed");
        }

        Files.createDirectories(uploadDir);
        String newName = UUID.randomUUID() + "." + extension;
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, uploadDir.resolve(newName), StandardCopyOption.REPLACE_EXISTING);
        }
        return newName;
    }

    // Returns the path of a stored file, or null if the name is unsafe
    public Path load(String fileName) {
        Path path = uploadDir.resolve(fileName).normalize();
        return path.startsWith(uploadDir) ? path : null;
    }
}