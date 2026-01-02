package com.example.Social.profile.controller;

import com.example.Social.profile.dto.createProfile;
import com.example.Social.profile.dto.updateCounters;
import com.example.Social.profile.dto.updateProfile;
import com.example.Social.profile.entity.profile;
import com.example.Social.profile.service.profileService;
import com.example.Social.profile.utils.jwtUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/profiles")   // All routes start with /api/profiles
@RequiredArgsConstructor
public class profileController {

    private final profileService profileService;
    private final jwtUtils jwtValidator;

    // CREATE profile (no JWT check)
    @PostMapping("/create-profile")
    public ResponseEntity<profile> createProfile(@RequestBody createProfile request) {
        profile profile = profileService.createProfile(request);
        return ResponseEntity.ok(profile);
    }

    // FETCH profile by userId (JWT required)
    @GetMapping("/{userId}")
    public ResponseEntity<?> fetchProfile(
            @PathVariable String userId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = extractToken(authHeader);

        if (token == null || !jwtValidator.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid token");
        }


        UUID userIdFromToken = jwtValidator.extractUserId(token);
        if (!userIdFromToken.toString().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed to access this profile");
        }

        profile profile = profileService.fetchProfile(userId);
        return ResponseEntity.ok(profile);
    }

    // UPDATE profile fields (JWT required)
    @PutMapping(value = "/update/{userId}", consumes = "multipart/form-data")
    public ResponseEntity<?> updateProfile(
            @PathVariable String userId,
            @RequestPart("data") updateProfile request,
            @RequestPart(value = "profilePic", required = false) MultipartFile profilePic,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = extractToken(authHeader);

        if (token == null || !jwtValidator.validateToken(token)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Missing or invalid token");
        }

        UUID userIdFromToken = jwtValidator.extractUserId(token);
        if (!userIdFromToken.toString().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not allowed to update this profile");
        }

        profile profile = profileService.updateProfile(userId, request, profilePic);
        return ResponseEntity.ok(profile);
    }


    // UPDATE counters (no JWT check)
    @PatchMapping("/{userId}/counters")
    public ResponseEntity<profile> updateCounters(
            @PathVariable String userId,
            @RequestBody updateCounters request
    ) {
        profile profile = profileService.updateCounters(userId, request);
        return ResponseEntity.ok(profile);
    }

    // ✅ Helper: Extract Bearer token
    private String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
