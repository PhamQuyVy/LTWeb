package com.hcmute.demo.service;

import com.hcmute.demo.entity.User;

import jakarta.servlet.http.Part;

public interface UserProfileService {
    User getProfile(int userId);
    void updateProfile(int userId, String fullName, String phone, Part avatarPart) throws Exception;
}