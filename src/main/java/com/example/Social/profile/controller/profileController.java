package com.example.Social.profile.controller;

import com.example.Social.profile.dto.InternalProfile;
import com.example.Social.profile.dto.createProfile;
import com.example.Social.profile.dto.fetchProfile;
import com.example.Social.profile.dto.updateProfile;
import com.example.Social.profile.entity.profile;
import com.example.Social.profile.service.profileService;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/profiles")   // All routes start with /api/profiles
@RequiredArgsConstructor
public class profileController {

    private final profileService profileService;

    @PostMapping("/create")
    public ResponseEntity<?> fetchOrCreateProfile(
            @RequestBody createProfile request
    ) {
        profile profile = profileService.fetchOrCreateProfile(request);

        return ResponseEntity.ok(profile);
    }


    // UPDATE profile fields (JWT required)
    @PutMapping(value = "/update", consumes = "multipart/form-data")
    public ResponseEntity<?> updateProfile(
            @RequestPart("data") updateProfile request,
            @RequestPart(value = "profilePic", required = false) MultipartFile profilePic,
            Authentication auth
    ) {

        String userId = auth.getName();
        profile profile = profileService.updateProfile(userId, request, profilePic);
        return ResponseEntity.ok(profile);
    }




    @PostMapping("/fetch-profile")
    public ResponseEntity<fetchProfile> fetch(
            Authentication auth
    ){

        String userId = auth.getName();

        fetchProfile data = profileService.getProfile(userId);

        return ResponseEntity.ok(data);

    }


    @GetMapping("/get/profile-stuff/{userId}")
    public ResponseEntity<InternalProfile> getInternalData(

            @PathVariable String userId

    ){

        InternalProfile res = profileService.getInternal(userId);

        return ResponseEntity.ok(res);
    }




}
