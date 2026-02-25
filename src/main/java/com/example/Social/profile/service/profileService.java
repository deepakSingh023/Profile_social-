package com.example.Social.profile.service;

import com.example.Social.profile.dto.createProfile;
import com.example.Social.profile.dto.fetchProfile;
import com.example.Social.profile.dto.updateProfile;
import com.example.Social.profile.entity.profile;
import org.springframework.web.multipart.MultipartFile;

public interface profileService {

    profile updateProfile(String userId, updateProfile data, MultipartFile newPic);

    profile fetchOrCreateProfile(createProfile data);


    fetchProfile getProfile(String userId);


}
