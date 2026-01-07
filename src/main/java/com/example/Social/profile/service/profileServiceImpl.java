package com.example.Social.profile.service;

import com.example.Social.profile.dto.createProfile;
import com.example.Social.profile.dto.updateCounters;
import com.example.Social.profile.dto.updateProfile;
import com.example.Social.profile.entity.profile;
import com.example.Social.profile.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class profileServiceImpl implements profileService {

    private final ProfileRepository profileRepository;
    private final R2ImageService r2ImageService;  // ✅ inject R2 service

    @Override
    public profile fetchOrCreateProfile(createProfile data, String userIdFromToken) {
        return profileRepository.findByUserId(userIdFromToken)
                .orElseGet(() -> {
                    profile p = new profile();
                    p.setUserId(userIdFromToken);
                    p.setUsername(data.getUsername());
                    p.setEmail(data.getEmail());
                    return profileRepository.save(p);
                });
    }


    @Transactional
    public profile updateProfile(String userId, updateProfile data, MultipartFile newPic) {

        profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (data.getBio() != null) {
            profile.setBio(data.getBio());
        }

        if (data.getPrivateAcc() != null) {
            profile.setPrivateAcc(data.getPrivateAcc());
        }

        if (newPic != null && !newPic.isEmpty()) {

            if (profile.getProfilePicUrl() != null && !profile.getProfilePicUrl().isEmpty()) {
                r2ImageService.deleteImage(profile.getProfilePicUrl());
            }

            String newUrl = r2ImageService.uploadProfilePic(newPic);
            profile.setProfilePicUrl(newUrl);
        }

        return profileRepository.save(profile);
    }

    @Transactional
    public profile updateCounters(String userId, updateCounters data) {
        profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        if (data.getFollowerCount() != null) {
            profile.setFollowerCount(profile.getFollowerCount() + data.getFollowerCount());
        }
        if (data.getFollowingCount() != null) {
            profile.setFollowingCount(profile.getFollowingCount() + data.getFollowingCount());
        }
        if (data.getFriendsCount() != null) {
            profile.setFriendsCount(profile.getFriendsCount() + data.getFriendsCount());
        }

        return profileRepository.save(profile);
    }
}
