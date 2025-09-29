package com.example.Social.profile.service;


import com.example.Social.profile.dto.createProfile;
import com.example.Social.profile.dto.updateCounters;
import com.example.Social.profile.dto.updateProfile;
import com.example.Social.profile.entity.profile;
import com.example.Social.profile.repository.ProfileRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class profileServiceImpl implements profileService {

    private final ProfileRepository profileRepository;


    public profile createProfile(createProfile data){

        profile profile = new profile();
        profile.setUserId(data.getUserId());
        profile.setEmail(data.getEmail());
        profile.setUsername(data.getUsername());

        return profileRepository.save(profile);

    }


    public profile fetchProfile(String userId) {
        return profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for userId: " + userId));
    }

    @Transactional
    public profile updateProfile(String userId, updateProfile data) {
        profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // Update only the fields provided
        if (data.getBio() != null) {
            profile.setBio(data.getBio());
        }
        if (data.getProfilePicUrl() != null) {
            profile.setProfilePicUrl(data.getProfilePicUrl());
        }
        if (data.getPrivateAcc() != null) {
            profile.setPrivateAcc(data.getPrivateAcc());
        }

        return profileRepository.save(profile);
    }

    @Transactional
    public profile updateCounters(String userId, updateCounters data) {
        profile profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Profile not found for userId: " + userId));

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



