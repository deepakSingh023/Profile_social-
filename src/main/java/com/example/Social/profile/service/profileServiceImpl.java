package com.example.Social.profile.service;

import com.example.Social.profile.dto.DenormalizeDto;
import com.example.Social.profile.dto.createProfile;
import com.example.Social.profile.dto.fetchProfile;
import com.example.Social.profile.dto.updateProfile;
import com.example.Social.profile.entity.profile;
import com.example.Social.profile.exceptions.ProfileNotFound;
import com.example.Social.profile.repository.ProfileRepository;

import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class profileServiceImpl implements profileService {

    private final ProfileRepository profileRepository;
    private final R2ImageService r2ImageService;
    private final MongoTemplate mongoTemplate;
    private final DenormalizeService denormalizeService;

    public  static final Logger log = LoggerFactory.getLogger(profileServiceImpl.class);





    @Override
    public profile fetchOrCreateProfile(createProfile data) {

        return profileRepository.findByUserId(data.getUserId())
                .orElseGet(() -> {
                    try {
                        profile p = new profile();
                        p.setUserId(data.getUserId());
                        p.setUsername(data.getUsername());
                        p.setEmail(data.getEmail());
                        return profileRepository.save(p);
                    } catch (DuplicateKeyException e) {
                        return profileRepository.findByUserId(data.getUserId())
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

        DenormalizeDto denorm = new DenormalizeDto(
                userId,
                profile.getProfilePicUrl()
        );

        try{
            denormalizeService.denormalize(denorm);

        }catch (Exception ex){
            log.info("denormalize of the image in reel and post service failed  ex={} userId={}",ex,userId);
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
