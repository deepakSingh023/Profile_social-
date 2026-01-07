package com.example.Social.profile.service;

import com.example.Social.profile.dto.createProfile;
import com.example.Social.profile.dto.updateCounters;
import com.example.Social.profile.dto.updateProfile;
import com.example.Social.profile.entity.profile;
import org.springframework.web.multipart.MultipartFile;

public interface profileService {

    profile updateProfile(String userId, updateProfile data, MultipartFile newPic);

    // ✅ change method to accept userId from token
    profile fetchOrCreateProfile(createProfile data, String userIdFromToken);

    profile updateCounters(String userId, updateCounters data);
}
