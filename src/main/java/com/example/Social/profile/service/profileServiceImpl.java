package com.example.Social.profile.service;

import com.example.Social.profile.dto.createProfile;
import com.example.Social.profile.dto.fetchProfile;
import com.example.Social.profile.dto.updateCounters;
import com.example.Social.profile.dto.updateProfile;
import com.example.Social.profile.entity.profile;
import com.example.Social.profile.exceptions.ProfileNotFound;
import com.example.Social.profile.repository.ProfileRepository;

import com.mongodb.DuplicateKeyException;
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
    private final R2ImageService r2ImageService;

    @Override
    public profile fetchOrCreateProfile(createProfile data, String userId) {

        return profileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    try {
                        profile p = new profile();
                        p.setUserId(userId);
                        p.setUsername(data.getUsername());
                        p.setEmail(data.getEmail());
                        return profileRepository.save(p);
                    } catch (DuplicateKeyException e) {
                        return profileRepository.findByUserId(userId)
                                .orElseThrow();
                    }
                });
    }



    @Transactional
    public profile updateProfile(String userId, updateProfile data, MultipartFile newPic) {

        profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFound("Profile not found"));

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
                .orElseThrow(() -> new ProfileNotFound("Profile not found"));

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


    @Override
    public fetchProfile getProfile(String userId) {

        profile data = profileRepository.findByUserId(userId)
                .orElseThrow(()-> new ProfileNotFound("profile not found"));

        fetchProfile res = new fetchProfile(
                userId,
                data.getUsername(),
                data.getBio(),
                data.getEmail(),
                data.getProfilePicUrl(),
                data.getPrivateAcc(),
                data.getFollowerCount(),
                data.getFollowingCount(),
                data.getFriendsCount()
        );

        return res;
    }
}
