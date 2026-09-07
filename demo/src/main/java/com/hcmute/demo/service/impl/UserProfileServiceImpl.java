package com.hcmute.demo.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import com.hcmute.demo.entity.User;
import com.hcmute.demo.repository.UserRepository;
import com.hcmute.demo.service.UserProfileService;
import com.hcmute.demo.util.Constant;

import jakarta.servlet.http.Part;

public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public User getProfile(int userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void updateProfile(int userId, String fullName, String phone, Part avatarPart) throws Exception {
        String savedAvatar = null;

        if (hasFile(avatarPart)) {
            savedAvatar = saveUpload(avatarPart);
        }

        try {
            userRepository.updateProfile(userId, fullName, phone, savedAvatar);
        } catch (Exception e) {
            if (savedAvatar != null) {
                deleteUploadedFile(savedAvatar);
            }
            throw e;
        }
    }

    private static boolean hasFile(Part part) {
        if (part == null) return false;
        String fileName = part.getSubmittedFileName();
        return fileName != null && !fileName.trim().isEmpty() && part.getSize() > 0;
    }

    private static String saveUpload(Part part) throws IOException {
        String originalName = Path.of(part.getSubmittedFileName()).getFileName().toString();
        String extension = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0) extension = originalName.substring(dot).toLowerCase();

        String fileName = "avatar_" + UUID.randomUUID() + extension;
        Path uploadDir = Path.of(Constant.DIR).toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);

        Path target = uploadDir.resolve(fileName).normalize();
        if (!target.startsWith(uploadDir)) {
            throw new IOException("Duong dan file khong hop le.");
        }

        try (InputStream input = part.getInputStream()) {
            Files.copy(input, target);
        }
        return fileName;
    }

    private static void deleteUploadedFile(String fileName) {
        try {
            Path uploadDir = Path.of(Constant.DIR).toAbsolutePath().normalize();
            Path file = uploadDir.resolve(fileName).normalize();
            if (file.startsWith(uploadDir)) {
                Files.deleteIfExists(file);
            }
        } catch (IOException ignored) {}
    }
}