package com.parkiyo.parkiyo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class ProfileImageStorageService {

    private static final long MAX_FILE_SIZE_BYTES = 5L * 1024 * 1024;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".webp");

    private final Path rootDirectory;

    public ProfileImageStorageService(
            @Value("${parkiyo.media.profile-dir:uploads/profile-images}") String profileDirectory) {
        this.rootDirectory = Paths.get(profileDirectory).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            throw new RuntimeException("Profile photo must be 5 MB or smaller.");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = extractExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new RuntimeException("Profile photo must be JPG, PNG, or WebP.");
        }

        String storedFilename = UUID.randomUUID() + extension;
        try {
            Files.createDirectories(rootDirectory);
            Path target = rootDirectory.resolve(storedFilename).normalize();
            if (!target.startsWith(rootDirectory)) {
                throw new RuntimeException("Invalid file path.");
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            }
            return storedFilename;
        } catch (IOException ex) {
            throw new RuntimeException("Unable to save profile photo.", ex);
        }
    }

    public void deleteIfExists(String filename) {
        if (!StringUtils.hasText(filename)) {
            return;
        }
        try {
            Path target = rootDirectory.resolve(filename).normalize();
            if (target.startsWith(rootDirectory)) {
                Files.deleteIfExists(target);
            }
        } catch (IOException ignored) {
        }
    }

    public Path resolve(String filename) {
        Path target = rootDirectory.resolve(filename).normalize();
        if (!target.startsWith(rootDirectory)) {
            throw new RuntimeException("Invalid file path.");
        }
        return target;
    }

    private String extractExtension(String filename) {
        String extension = StringUtils.getFilenameExtension(filename);
        if (!StringUtils.hasText(extension)) {
            throw new RuntimeException("Profile photo must include a valid file extension.");
        }
        return "." + extension.toLowerCase(Locale.ROOT);
    }
}
