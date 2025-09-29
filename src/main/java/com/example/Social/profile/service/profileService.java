package com.example.Social.profile.service;


import com.example.Social.profile.dto.createProfile;
import com.example.Social.profile.dto.updateCounters;
import com.example.Social.profile.dto.updateProfile;
import com.example.Social.profile.entity.profile;

public interface profileService {


    profile  createProfile(createProfile data);

    profile updateProfile(String userId , updateProfile data);

    profile fetchProfile(String userId);

    profile updateCounters(String userId , updateCounters data);
}
